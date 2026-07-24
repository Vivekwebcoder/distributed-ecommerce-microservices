package com.ecommerce.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}