package com.gaston.springcloud.msvc.items.services;


import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.gaston.springcloud.msvc.items.models.Item;
import com.gaston.springcloud.msvc.items.models.Product;


@Service
public class ItemServiceWebClient implements ItemService {

    private final WebClient.Builder webClient;
    
    public ItemServiceWebClient(Builder webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Item> findAll() {
        return webClient.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> new Item(product, new Random().nextInt(1, 11)))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        // try {
             return Optional.of(webClient.build()
                .get()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class)
                .map(product -> new Item(product, new Random().nextInt(1, 11)))
                .block());

        // } catch (WebClientResponseException e) {
        //     return Optional.empty();
        // }
       
    }

    @Override
    public Product save(Product product) {
        return webClient.build()
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    @Override
    public Product update(Long id, Product product) {
        return webClient.build()
                .put()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }
    
    @Override
    public void deleteById(Long id) {
        webClient.build()
                .delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }


}
