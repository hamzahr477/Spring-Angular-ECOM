package lu.atozdigital.api.controller;

import lu.atozdigital.api.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
