package com.ecommerce.product.controller;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.product.model.Product;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.service.ProductService;
@CrossOrigin(origins = "http://localhost:5173")
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
@GetMapping("/products")
public List<Product> getAllProducts() {
    return productService.getAllProducts();
}
@GetMapping("/products/{id}")
public Product getProductById(@PathVariable String id) {
    return productService.getProductById(id);
}
@PutMapping("/products/{id}")
public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
    return productService.updateProduct(id, product);
}
@DeleteMapping("/products/{id}")
public String deleteProduct(@PathVariable String id) {
    return productService.deleteProduct(id);
}
@GetMapping("/products/search")
public List<Product> searchProducts(@RequestParam String keyword) {
    return productService.searchProducts(keyword);
}
}