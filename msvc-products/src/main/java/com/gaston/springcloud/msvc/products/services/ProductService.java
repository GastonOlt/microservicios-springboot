package com.gaston.springcloud.msvc.products.services;

import java.util.List;
import java.util.Optional;

import com.gaston.springcloud.msvc.products.entities.Product;

public interface ProductService {
     
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    Product update(Long id, Product product);
    void deleteById(Long id);
}
