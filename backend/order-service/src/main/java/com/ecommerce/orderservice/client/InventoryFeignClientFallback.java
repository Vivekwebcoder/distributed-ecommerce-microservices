package com.ecommerce.orderservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryFeignClientFallback implements InventoryFeignClient {

    @Override
    public Boolean checkStock(String skuCode, Integer quantity) {
        log.warn("Inventory service unavailable. Fallback executed for checkStock: skuCode={}, quantity={}", skuCode, quantity);
        return true;
    }
}
