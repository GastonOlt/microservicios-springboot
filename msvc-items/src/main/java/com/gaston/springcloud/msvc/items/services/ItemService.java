package com.gaston.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;

import com.gaston.springcloud.msvc.items.models.Item;

public interface ItemService {
    
    List<Item> findAll();
    Optional<Item> findById(Long id);
}
