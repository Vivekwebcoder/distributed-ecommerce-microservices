package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
 List<Product> findByNameContainingIgnoreCase(String keyword);
}