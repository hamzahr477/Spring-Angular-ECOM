package lu.atozdigital.api.controller;

import lu.atozdigital.api.exception.application.ResourceNotFoundException;
import lu.atozdigital.api.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * This is commandes controller, handle CRUD of commande
 *
 * @author hamza_hayar
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    /**
     * Declaration of commande service with injection
     */
    @Autowired
    CommandeService commandeService;

    /**
     * Get All Orders saved in database
     * @return a Response Entity with List of Orders
     */
    @GetMapping("")
    public ResponseEntity<?> getAllOrder(){
        return new ResponseEntity<>(commandeService.getAllCommandes(), HttpStatus.OK);
    }
    /**
     * Get order by Id from Database
     * @param id a Long contain id of Order
     * @return a Response Entity contain the Order looking for
     * @throws ResourceNotFoundException if not exist order with that id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(commandeService.getCommande(id),HttpStatus.OK);
    }
}
