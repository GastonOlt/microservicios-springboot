package com.gaston.springcloud.msvc.products.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.gaston.libs.msvc.commons.entities.Product;
import com.gaston.springcloud.msvc.products.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class ProductController {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping()
    public ResponseEntity<List<Product>> list() {
        logger.info("ingresando al metodo del controller ProductController::list()");
        return ResponseEntity.ok(productService.findAll());
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) {
        logger.info("ingresando al metodo del controller ProductController::details() con id: {}", id);
        if(id.equals(101L)){
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
    
    @PostMapping()
    public ResponseEntity<Product> create(@RequestBody Product product) {
        logger.info("Product creando: {}", product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Ingresando metodo ProductController::update con id: {} y product: {}", id, product);
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product productDb = productOptional.orElseThrow();

            productDb.setName(product.getName());
            productDb.setPrice(product.getPrice());
            productDb.setCreateAt(product.getCreateAt());
            Product updatedProduct = productService.save(productDb);

            return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Product eliminando con id: {}", id);
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
