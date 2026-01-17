package com.gaston.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;

import com.gaston.springcloud.msvc.items.models.Item;
import com.gaston.springcloud.msvc.items.models.Product;

public interface ItemService {
    
    List<Item> findAll();
    Optional<Item> findById(Long id);
    Product save(Product product);
    Product update(Long id, Product product);
    void deleteById(Long id);
}
