package com.ecommerce.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service", fallback = CartFeignClientFallback.class)
public interface CartFeignClient {

    @DeleteMapping("/api/v1/cart/clear")
    void clearCart(@RequestHeader("X-User-Id") String userId);
}
