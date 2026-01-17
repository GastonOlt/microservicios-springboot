package com.gaston.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.gaston.libs.msvc.commons.entities.Product;


public interface ProductRepository extends CrudRepository<Product,Long> {

}
