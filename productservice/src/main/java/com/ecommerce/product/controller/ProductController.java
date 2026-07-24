package com.ecommerce.product.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.product.model.Product;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/products")
public Product addProduct(@RequestBody Product product) {
    return productService.addProduct(product);
}
}