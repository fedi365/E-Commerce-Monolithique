package org.example.ecommercemonolithique.controller;

import org.example.ecommercemonolithique.model.Product;
import org.example.ecommercemonolithique.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductServices productServices;

    // Create
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product created = productServices.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productServices.getAllProducts());
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productServices.getProductById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productServices.updateProduct(id, product));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productServices.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // calcul Promo
    @GetMapping("/promo/{id}")
    public ResponseEntity<Double> calculatePromo(@PathVariable Long id) {
        try {
            Product product = productServices.getProductById(id);
            double promo = productServices.calculatePromo(product);
            return ResponseEntity.ok(promo);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
