package lu.atozdigital.api;
import lu.atozdigital.api.dto.CommandeDTO;
import lu.atozdigital.api.dto.ProductCommandeDTO;
import lu.atozdigital.api.dto.ProductDTO;
import lu.atozdigital.api.enums.PayementMethod;
import lu.atozdigital.api.exception.application.ResourceNotFoundException;
import lu.atozdigital.api.exception.business.QuantityInsufficientException;
import lu.atozdigital.api.service.CommandeService;
import lu.atozdigital.api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class DemoApplicationTests {

    @Autowired
    ProductService productServiceTest;
    @Autowired
    CommandeService commandeServiceTest;
    @Test
    void checkSavingProducts() throws ResourceNotFoundException {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("product 1")
                .urlImg("url image 1")
                .quantity(2)
                .price(299.99)
                .build();

        //when
        ProductDTO product = productServiceTest.saveProduct(productDTO);

        //then
        assertThat(product.getName()).isEqualTo( productDTO.getName());
        assertThat(product.getPrice()).isEqualTo( productDTO.getPrice());
        assertThat(product.getUrlImg()).isEqualTo( productDTO.getUrlImg());

        //end
        this.productServiceTest.deletProduct(product.getId());
    }
    @Test
    void checkGetProductById() throws ResourceNotFoundException {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("product 1")
                .urlImg("url image 1")
                .quantity(2)
                .price(299.99)
                .build();

        //when
        ProductDTO p1 = productServiceTest.saveProduct(productDTO);
        ProductDTO p2 = productServiceTest.getProduct(p1.getId());

        //then
        assertThat(p1.getId()).isEqualTo( p2.getId());
        assertThat(p1.getName()).isEqualTo( p2.getName());
        assertThat(p1.getPrice()).isEqualTo( p2.getPrice());
        assertThat(p1.getUrlImg()).isEqualTo( p2.getUrlImg());

        //end
        this.productServiceTest.deletProduct(p1.getId());
    }
    @Test
    void checkGetAllProduct() throws ResourceNotFoundException {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("product 2")
                .urlImg("url image 2")
                .quantity(2)
                .price(32.99)
                .build();

        //when
        ProductDTO p1 = productServiceTest.saveProduct(productDTO);
        List<ProductDTO> productDTOList = productServiceTest.getAllProducts();

        //then
        assertThat(productDTOList).anyMatch(product -> product.getName().equals(productDTO.getName()) );

        //end
        this.productServiceTest.deletProduct(p1.getId());
    }
    @Test
    void checkSavingOrder() throws ResourceNotFoundException, QuantityInsufficientException {

        //given
        int quantityInit = 5;
        int quantityInOrder = 4;
        ProductDTO productDTO = ProductDTO.builder()
                .name("product 1")
                .urlImg("url image 1")
                .quantity(quantityInit)
                .price(299.99)
                .build();
        CommandeDTO commandeDTO = CommandeDTO.builder()
                .productCommandeList(
                        Arrays.asList(ProductCommandeDTO.builder().quantity(quantityInOrder)
                                .product(ProductDTO.builder().id(1L).build()).build()))
                .payementMethod(PayementMethod.Paypal)
                .build();

        //when
        productServiceTest.saveProduct(productDTO);
        CommandeDTO res = commandeServiceTest.saveCommande(commandeDTO);
        ProductDTO product = productServiceTest.getProduct(1L);

        //then
        assertThat(product.getQuantity()).isEqualTo(quantityInit-quantityInOrder);
        assertThat(res.getId()).isNotNull();
        assertThat(res.getTotal()).isEqualTo(product.getPrice() * quantityInOrder);

        //end
        this.commandeServiceTest.deleteCommande(res.getId());
        this.productServiceTest.deletProduct(product.getId());
    }
    @Test
    void checkGetOrderById() throws ResourceNotFoundException, QuantityInsufficientException {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("product 1")
                .urlImg("url image 1")
                .price(299.99)
                .quantity(3)
                .build();
        ProductDTO p1  =productServiceTest.saveProduct(productDTO);

        CommandeDTO commandeDTO = CommandeDTO.builder()
                .productCommandeList(
                        Arrays.asList(ProductCommandeDTO.builder().quantity(2)
                                .product(p1).build()))
                .payementMethod(PayementMethod.Paypal)
                .build();


        //when
        CommandeDTO res = commandeServiceTest.saveCommande(commandeDTO);

        //then
        assertThat(commandeServiceTest.getCommande(res.getId())).isNotNull();


        //end
        this.commandeServiceTest.deleteCommande(res.getId());
        this.productServiceTest.deletProduct(p1.getId());
    }
    @Test
    void checkGetAllOders() throws ResourceNotFoundException, QuantityInsufficientException {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("product 3")
                .urlImg("url image 3")
                .quantity(3)
                .price(129.99)
                .build();
        ProductDTO p1 = productServiceTest.saveProduct(productDTO);

        List<CommandeDTO> commandeDTOs = Arrays.asList(CommandeDTO.builder()
                        .productCommandeList(
                                List.of(ProductCommandeDTO.builder().quantity(2)
                                        .product(p1).build()))
                        .payementMethod(PayementMethod.Paypal)
                        .build(),
                CommandeDTO.builder()
                        .productCommandeList(
                                List.of(ProductCommandeDTO.builder().quantity(1)
                                        .product(p1).build()))
                        .payementMethod(PayementMethod.COD)
                        .build());


        //when
        commandeServiceTest.saveCommande(commandeDTOs.get(0));
        commandeServiceTest.saveCommande(commandeDTOs.get(1));
        List<CommandeDTO> res = commandeServiceTest.getAllCommandes();


        //then
        assertThat(res).isNotEmpty();


        //end
        for (CommandeDTO c:res) {
            this.commandeServiceTest.deleteCommande(c.getId());
        }
        this.productServiceTest.deletProduct(p1.getId());
    }

}
