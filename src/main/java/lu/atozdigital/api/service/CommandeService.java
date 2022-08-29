package lu.atozdigital.api.service;

import lu.atozdigital.api.dto.CommandeDTO;
import lu.atozdigital.api.dto.ProductCommandeDTO;
import lu.atozdigital.api.entity.Commande;
import lu.atozdigital.api.entity.Product;
import lu.atozdigital.api.entity.ProductCommande;
import lu.atozdigital.api.exception.application.ResourceNotFoundException;
import lu.atozdigital.api.exception.business.EmptyOrderException;
import lu.atozdigital.api.exception.business.QuantityInsufficientException;
import lu.atozdigital.api.repository.CommandeRepository;
import lu.atozdigital.api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommandeService {
    /**
     * Daclaration of orderRepository to 'CRUD' order
     */
    @Autowired
    CommandeRepository orderRepository;
    /**
     * Declaration of productRepository to 'CRUD' product
     */
    @Autowired
    ProductRepository productRepository;
    /**
     * Declaration of modelMapper to mapping between DTO and Entity
     */
    ModelMapper modelMapper = new ModelMapper();

    /**
     * Get All Orders from database
     * @return List of CommandeDTO
     */
    public List<CommandeDTO> getAllCommandes(){
        return orderRepository.findAll().stream().map(order ->modelMapper.map(order, CommandeDTO.class)).collect(Collectors.toList());
    }

    /**
     * Save Commande into database
     * Step 1:
     *  Convert CommandeDTO to Entity
     * Step 2:
     *  Check if products exist in database
     *  Subtract from quantity of product the quantity ordered
     *  Cumulate the total
     * Step 3:
     *  Set date of order
     *  Generate a Reference code for order
     *  Save Order
     *
     *
     * @param commandeDTO Object of CommandDTO contain information of Order
     * @return CommandDTO contain order saved
     * @throws ResourceNotFoundException if order contain product that not exist in database
     * @throws QuantityInsufficientException if Quantity of order great than quantity of Product
     */
    public CommandeDTO saveCommande(CommandeDTO commandeDTO) throws ResourceNotFoundException, QuantityInsufficientException {
        Commande commande = commandeDTO.toEntity();
        for (ProductCommande productCommande : commande.getProductCommandeList()) {
            Product product;
            product = productRepository.findById(productCommande.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Order with id::" + productCommande.getProduct().getId() + " Not found"));
            if(product.getQuantity() < productCommande.getQuantity())
                throw new QuantityInsufficientException("Insufficient quantity for product "+product.getName());
            else
                product.setQuantity(product.getQuantity()-productCommande.getQuantity());
            productRepository.save(product);
            commande.setTotal(commande.getTotal() + product.getPrice() * productCommande.getQuantity());
        }
        commande.setOrderDate(LocalDateTime.now());
        commande.setNumOrder(UUID.randomUUID().toString().replace("-", ""));
        return modelMapper.map(orderRepository.save(commande),CommandeDTO.class);
    }

    /**
     * Edit order alrady saved
     * Step 1:
     *  Get old Commande
     *  Set new method of payement and reinitialize total with 0
     * Step 2:
     *  Check if new Order is Empty
     * Step 3:
     *  Get the products
     *  Add quantity ordered to quantity of product
     *  Subtract the new Quantity ordered
     *  Save products
     * Step 4:
     *  Save the new Commande
     * @param commandeDTO Object of CommandDTO contain information of Order
     * @return CommandDTO contain order edited
     * @throws ResourceNotFoundException if order contain product that not exist in database
     * @throws QuantityInsufficientException if Quantity of order great than quantity of Product
     * @throws EmptyOrderException if Order contain no product
     */
    public CommandeDTO editCommande(CommandeDTO commandeDTO) throws ResourceNotFoundException, QuantityInsufficientException, EmptyOrderException {
        Commande commande = orderRepository.findById(commandeDTO.getId()).orElseThrow(()-> new ResourceNotFoundException("Product with id::"+commandeDTO.getId()+" Not found"));
        commande.setPayementMethod(commandeDTO.getPayementMethod());
        commande.setTotal((double) 0);
        if(commandeDTO.getProductCommandeList().size()==0){
            throw new EmptyOrderException("Order cannot by empty !!");
        }
        for (ProductCommandeDTO productCommande : commandeDTO.getProductCommandeList()) {
            List<ProductCommande> productCommandeOldList =  commande.getProductCommandeList().stream().filter(productCommande1 ->
                    Objects.equals(productCommande1.getId(), productCommande.getId())).toList();
            Product product;
            product = productRepository.findById(productCommande.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Order with id::" + productCommande.getProduct().getId() + " Not found"));
            product.setQuantity(product.getQuantity()+(productCommandeOldList.isEmpty() ? 0:productCommandeOldList.get(0).getQuantity()) );
            if(product.getQuantity() < productCommande.getQuantity())
                throw new QuantityInsufficientException("Insufficient quantity for product "+product.getName());
            else
                product.setQuantity(product.getQuantity()-productCommande.getQuantity());
            productRepository.save(product);
            commande.setTotal(product.getPrice() * productCommande.getQuantity());
        }
        commande.setProductCommandeList(commandeDTO.toEntity().getProductCommandeList());
        return modelMapper.map(orderRepository.save(commande),CommandeDTO.class);
    }

    /**
     * Delet order from database
     * @param id a Long contain id of Order
     * @throws ResourceNotFoundException if order that not exist in database
     */
    public void deleteCommande(Long id) throws ResourceNotFoundException {
        orderRepository.delete(orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order with id::"+id+" Not found")));
    }

    /**
     * Research for oders with specific parameters and return them with pagination
     * @param offset integer contain number of current page
     * @param pageSize integer contain number of item in page
     * @param sortField string contain the field of order that sorting will apply
     * @param sortType string ('desc';'asc') contain direction of sorting the commandes
     * @param numOrder string contain a code of Order
     * @return Response Entity contain pagination of commandes
     */
    public Page<CommandeDTO> search(String sortField, int offset, int pageSize, String sortType, String numOrder) {
        return orderRepository.findAllByNumOrderContains(numOrder, PageRequest.of(offset,pageSize, Sort.by(Sort.Direction.fromString(sortType),sortField))).map(commande -> modelMapper.map(commande,CommandeDTO.class));
    }

    /**
     * Get order by id
     * @param id a Long contain id of Order
     * @return CommandDTO contain information of order
     * @throws ResourceNotFoundException if order that not exist in database
     */
    public CommandeDTO getCommande(Long id) throws ResourceNotFoundException {
        return modelMapper.map(orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order with id::"+id+" Not found")),CommandeDTO.class);
    }
}
