package com.gaston.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.gaston.libs.msvc.commons.entities.Product;
import com.gaston.springcloud.msvc.items.models.Item;
import com.gaston.springcloud.msvc.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RefreshScope
@RestController
public class ItemController {

    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Value("${configuracion.texto}")
    private String msg;

    public ItemController(@Qualifier("itemServiceFeign") ItemService itemService, CircuitBreakerFactory circuitBreakerFactory) {
        this.itemService = itemService;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/fetch-configs")
    public String fetchConfigs() {
        return msg;
    }
    
    @GetMapping
    public List<Item> list(@RequestParam(name = "nombre", required = false) String nombre,
                            @RequestHeader(name = "token-request", required = false) String token) {
        logger.info("llamada al metodo del controller ItemController::list()");
        logger.info("Request Parameter: {}", nombre);
        logger.info("Token: {}", token);
            
        System.out.println(nombre);
        System.out.println(token);
        return itemService.findAll();
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Item> details(@PathVariable Long id) {
        Optional<Item> itemOptional =  circuitBreakerFactory.create("items")
            .run(() -> itemService.findById(id), e -> {
                logger.error(e.getMessage());
                
                Product product = new Product();
                product.setId(1L);
                product.setCreateAt(LocalDate.now());
                product.setName("Camara Sony");
                product.setPrice(500.00);
                return Optional.of(new Item(product, 3));
            });

        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
    @CircuitBreaker(name = "items",fallbackMethod = "fallbackMethod")
    @GetMapping("/details/{id}")
    public ResponseEntity<Item> details2(@PathVariable Long id) {
        Optional<Item> itemOptional = itemService.findById(id);

        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @CircuitBreaker(name = "items", fallbackMethod = "fallbackMethod2")
    @TimeLimiter (name = "items")
    @GetMapping("/details2/{id}")
    public CompletableFuture<?> details3(@PathVariable Long id) {
       return CompletableFuture.supplyAsync(() -> {
            Optional<Item> itemOptional = itemService.findById(id);

            if (itemOptional.isPresent()) {
                return ResponseEntity.ok(itemOptional.orElseThrow());
            }
            return ResponseEntity.notFound().build();
        });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        logger.info("Product creando: {}", product);
        return itemService.save(product);
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product update(@PathVariable Long id, @RequestBody Product entity) {
        logger.info("Product actualizando: {}", entity);
        return itemService.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        logger.info("Product eliminando con id: {}", id);
        itemService.deleteById(id);
    }

    public ResponseEntity<Item> fallbackMethod(Throwable e) {
        logger.error(e.getMessage());
        
        Product product = new Product();
        product.setId(1L);
        product.setCreateAt(LocalDate.now());
        product.setName("Camara Sony Nueva");
        product.setPrice(500.00);
        Item item = new Item(product, 3);
        return ResponseEntity.ok(item);
    }

    public CompletableFuture<?> fallbackMethod2(Throwable e) {
        return CompletableFuture.supplyAsync(() -> {
            logger.error(e.getMessage());

            Product product = new Product();
            product.setId(1L);
            product.setCreateAt(LocalDate.now());
            product.setName("Camara Sony fallbackMethod2");
            product.setPrice(500.00);
            Item item = new Item(product, 3);
            return ResponseEntity.ok(item);
        });
    }
}
