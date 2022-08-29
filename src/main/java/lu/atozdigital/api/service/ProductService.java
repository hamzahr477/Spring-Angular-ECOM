package lu.atozdigital.api.service;

import lu.atozdigital.api.dto.ProductDTO;
import lu.atozdigital.api.entity.Product;
import lu.atozdigital.api.exception.application.ResourceNotFoundException;
import lu.atozdigital.api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class ProductService {
    /**
     * Declaration of productRepository to 'CRUD' product
     */
    @Autowired
    ProductRepository productRepository;

    @Autowired
    FileService fileService;
    /**
     * Declaration of modelMapper to mapping between DTO and Entity
     */
    ModelMapper modelMapper = new ModelMapper();

    /**
     * Get All Products from database
     * @return List of ProductDTO
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(product ->modelMapper.map(product,ProductDTO.class)).collect(Collectors.toList());
    }

    /**
     * Get product by id
     * @param id a Long contain id of product
     * @return ProductDTO contain product information
     * @throws ResourceNotFoundException if product not exist for that id
     */
    public ProductDTO getProduct(Long id) throws ResourceNotFoundException {
        return modelMapper.map(productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product with id::"+id+" Not found")),ProductDTO.class);
    }

    /**
     * Save Prodect in database
     * @param productDTO a ProductDTO contain informatoin of Product
     * @return ProductDTO contain new product saved
     */
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        return modelMapper.map(productRepository.save(product),ProductDTO.class);
    }

    /**
     * Edit product already exist
     *
     * @param productDTO a ProductDTO contain new informations
     * @return productDTO contain product information after edited
     * @throws ResourceNotFoundException if product not found
     */
    public ProductDTO editProduct(ProductDTO productDTO) throws ResourceNotFoundException {
        Product product = modelMapper.map(productDTO, Product.class);
        Product product_Old = productRepository.findById(product.getId()).orElseThrow(()-> new ResourceNotFoundException("Product with id::"+product.getId()+" Not found"));
        product_Old.setName(product.getName());
        product_Old.setDescription(product.getDescription());
        product_Old.setQuantity(product.getQuantity());
        product_Old.setPrice(product.getPrice());
        product_Old.setUrlImg(product.getUrlImg());
        return modelMapper.map(productRepository.save(product_Old),ProductDTO.class);
    }
    /**
     * Delete product from database
     * @param id a Long contain id of Product
     * @throws ResourceNotFoundException if product that not exist in database
     */
    public void deletProduct(Long id) throws ResourceNotFoundException {
        productRepository.delete(productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product with id::"+id+" Not found")));
    }
    /**
     * Research for products with specific parameters and return them with pagination
     * @param offset integer contain number of current page
     * @param pageSize integer contain number of item in page
     * @param sortField string contain the field of order that sorting will apply
     * @param sortType string ('desc';'asc') contain direction of sorting the products
     * @param name string contain search key
     * @return Response Entity contain pagination of products
     */
    public Page<ProductDTO> search(String sortField, int offset, int pageSize, String sortType, String name) {
        return productRepository.findAllByNameContains(name,PageRequest.of(offset,pageSize, Sort.by(Sort.Direction.fromString(sortType),sortField))).map(product ->modelMapper.map(product,ProductDTO.class));
    }

    /**
     * Save product image in server
     * @param id a Long contain id of product
     * @param image Multipart file contain image of product
     * @return ProductDTO contain information of new iformations
     * @throws ResourceNotFoundException if product that not exist in database
     * @throws IOException if cannot save image in server cause of path or type
     */
    public ProductDTO saveImage(Long id, MultipartFile image) throws ResourceNotFoundException, IOException {
        Product product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product with id::"+id+" Not found"));
        if(image != null)
            product.setUrlImg(fileService.uploadImage(id,image));
        return modelMapper.map(productRepository.save(product),ProductDTO.class);
    }
}
