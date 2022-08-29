package lu.atozdigital.api.controller;

import lu.atozdigital.api.dto.ProductDTO;
import lu.atozdigital.api.exception.application.ResourceNotFoundException;
import lu.atozdigital.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ProductController Class Controller handle CRUD for PRODUCT
 * @author Hamza_Hayat
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/articles")
@Validated
public class ArticleController {
    /**
     * productService a Service contain all method form product management
     */
    @Autowired
    ProductService productService;

    /**
     * Get All product saved in database
     * @return Response Entity contain List of Products
     */
    @GetMapping("")
    ResponseEntity<?> getAllProduct(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    /**
     * Get product by Id
     * @param id a Long contain id of product looking for
     * @return Response Entity contain the Product
     * @throws ResourceNotFoundException if no product exist with that id
     */
    @GetMapping("/{id}")
    ResponseEntity<?> getProductById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
    }
    /**
     * Save a new Product in database
     * @param product a Object of ProductDTO contain informations of product
     * @return Response Entity contain new product saved
     */
    @PostMapping("")
    ResponseEntity<?> saveProduct( @RequestBody @Valid ProductDTO product){
        return new ResponseEntity<>(productService.saveProduct(product),HttpStatus.CREATED);
    }
    /**
     * Edit product already exist in database
     * @param product a Object of ProductDTO contain informations of product
     * @return Response Entity contain product after edited
     * @throws ResourceNotFoundException if cannot find the product
     */
    @PutMapping("")
    ResponseEntity<?> editProduct( @RequestBody @Valid ProductDTO product) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.editProduct(product),HttpStatus.ACCEPTED);
    }
    /**
     * Remove product from database
     * @param id a Long contain id of product
     * @return Response Entity
     * @throws ResourceNotFoundException if cannot find the product with that id
     */
    @DeleteMapping("")
    ResponseEntity<?> deleteProduct(@RequestParam(name = "id" ,required = true) Long id ) throws ResourceNotFoundException {
        productService.deletProduct(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
