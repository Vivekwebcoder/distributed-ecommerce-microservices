package com.ecommerce.orderservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CartFeignClientFallback implements CartFeignClient {

    @Override
    public void clearCart(String userId) {
        log.warn("Cart service unavailable. Fallback executed for clearCart for userId: {}", userId);
    }
}
