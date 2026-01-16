package com.gaston.springcloud.msvc.products.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaston.springcloud.msvc.products.entities.Product;
import com.gaston.springcloud.msvc.products.repositories.ProductRepository;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final Environment environment;

    public ProductServiceImp(ProductRepository productRepository, Environment environment) {
        this.productRepository = productRepository;
        this.environment = environment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((List<Product>) productRepository.findAll()).stream().map(product -> {
            product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return product;
        }).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return product;
        });
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public Product update(Long id, Product product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


}
