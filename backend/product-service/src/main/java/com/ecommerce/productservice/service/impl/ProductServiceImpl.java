package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.mapper.ProductMapper;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse addProduct(ProductRequest request) {
        log.info("Adding new product with name: {}", request.getName());
        Product product = productMapper.toEntity(request);
        if (product.getActive() == null) {
            product.setActive(true);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseList(products);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with id: {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        productMapper.updateEntityFromRequest(request, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        log.info("Searching products with keyword: {}", keyword);
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products in category: {}", category);
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        return productMapper.toResponseList(products);
    }
}
