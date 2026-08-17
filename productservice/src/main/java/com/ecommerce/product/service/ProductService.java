package com.ecommerce.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public Product addProduct(Product product) {
    return productRepository.save(product);
    
}
public List<Product> getAllProducts() {
    return productRepository.findAll();
}
public Product getProductById(String id) {
    return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
}
public Product updateProduct(String id, Product updatedProduct) {

    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

    existingProduct.setName(updatedProduct.getName());
    existingProduct.setBrand(updatedProduct.getBrand());
    existingProduct.setCategory(updatedProduct.getCategory());
    existingProduct.setDescription(updatedProduct.getDescription());
    existingProduct.setPrice(updatedProduct.getPrice());
    existingProduct.setStock(updatedProduct.getStock());
    existingProduct.setSeller(updatedProduct.getSeller());
    existingProduct.setImageUrl(updatedProduct.getImageUrl());
    existingProduct.setActive(updatedProduct.isActive());

    return productRepository.save(existingProduct);
}
public String deleteProduct(String id) {

    if (!productRepository.existsById(id)) {
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    productRepository.deleteById(id);
    return "Product deleted successfully.";
}
public List<Product> searchProducts(String keyword) {
    return productRepository.findByNameContainingIgnoreCase(keyword);
}
}