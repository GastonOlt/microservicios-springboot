package com.gaston.springcloud.msvc.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.gaston.springcloud.msvc.items.models.Product;

@FeignClient(name = "msvc-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    Product details(@PathVariable("id") Long id);

    @PostMapping
    Product save(Product product);

    @PutMapping("/{id}")
    Product update(@PathVariable("id") Long id, Product product);

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable("id") Long id);
    
}
 