package com.gaston.springcloud.msvc.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gaston.springcloud.msvc.items.models.Product;

@FeignClient(url = "localhost:8001/products", name = "mscv-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("{id}")
    Product details(@PathVariable Long id);
}
 