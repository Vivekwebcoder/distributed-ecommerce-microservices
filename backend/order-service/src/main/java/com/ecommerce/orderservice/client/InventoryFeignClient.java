package com.ecommerce.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", fallback = InventoryFeignClientFallback.class)
public interface InventoryFeignClient {

    @GetMapping("/api/v1/inventory/check")
    Boolean checkStock(@RequestParam("skuCode") String skuCode, @RequestParam("quantity") Integer quantity);
}
