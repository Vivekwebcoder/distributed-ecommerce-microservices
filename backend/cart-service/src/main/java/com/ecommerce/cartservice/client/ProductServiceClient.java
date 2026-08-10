package com.ecommerce.cartservice.client;

import com.ecommerce.cartservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", fallback = ProductServiceClientFallback.class)
public interface ProductServiceClient {

    @GetMapping("/api/v1/products/{id}")
    ProductDto getProductById(@PathVariable("id") String id);
}
