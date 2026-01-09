package com.gaston.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.gaston.springcloud.msvc.items.clients.ProductFeignClient;
import com.gaston.springcloud.msvc.items.models.Item;
import com.gaston.springcloud.msvc.items.models.Product;

import feign.FeignException;

@Service
public class ItemServiceFeign implements ItemService {

    private final ProductFeignClient productFeignClient;

    public ItemServiceFeign(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @Override
    public List<Item> findAll() {
        return productFeignClient.findAll()
                .stream()
                .map(product -> new Item(product, new Random().nextInt(1, 11)))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        try {
            Product product = productFeignClient.details(id);
            return Optional.of(new Item(product, new Random().nextInt(1, 11)));
        } catch (FeignException e) {
            return Optional.empty();
        }
    }

}
