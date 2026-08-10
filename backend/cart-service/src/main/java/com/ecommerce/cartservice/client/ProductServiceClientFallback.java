package com.ecommerce.cartservice.client;

import com.ecommerce.cartservice.dto.ProductDto;
import com.ecommerce.cartservice.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductServiceClientFallback implements ProductServiceClient {

    @Override
    public ProductDto getProductById(String id) {
        log.error("Fallback triggered for ProductServiceClient#getProductById with productId: {}. Product Service may be unavailable.", id);
        throw new ProductNotFoundException("Product Service is currently unavailable or product with ID " + id + " could not be retrieved.");
    }
}
