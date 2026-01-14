package com.gaston.springcloud.msvc.products.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.gaston.springcloud.msvc.products.entities.Product;
import com.gaston.springcloud.msvc.products.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping()
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productService.findAll());
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) {
        
        if(id.equals(10L)){
            throw new IllegalStateException("Product not found");
        }
        if(id.equals(7L)){
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                // Thread.currentThread().interrupt();
            }
        }
        
        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
}
