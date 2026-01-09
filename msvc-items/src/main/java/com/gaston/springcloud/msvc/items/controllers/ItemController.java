package com.gaston.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.gaston.springcloud.msvc.items.models.Item;
import com.gaston.springcloud.msvc.items.services.ItemService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(@Qualifier("itemServiceWebClient") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> list() {
        return itemService.findAll();
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Item> details(@PathVariable Long id) {
        Optional<Item> itemOptional = itemService.findById(id);
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
}
