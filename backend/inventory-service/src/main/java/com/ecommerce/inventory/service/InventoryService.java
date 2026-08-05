package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.event.OrderCancelledEvent;
import com.ecommerce.inventory.dto.event.OrderCreatedEvent;
import com.ecommerce.inventory.dto.request.InventoryCreateRequest;
import com.ecommerce.inventory.dto.request.StockCheckRequest;
import com.ecommerce.inventory.dto.request.StockUpdateRequest;
import com.ecommerce.inventory.dto.response.InventoryResponse;
import com.ecommerce.inventory.dto.response.StockCheckResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse createInventory(InventoryCreateRequest request);

    InventoryResponse getInventoryBySku(String skuCode);

    InventoryResponse updateStock(String skuCode, StockUpdateRequest request);

    InventoryResponse reduceStock(StockUpdateRequest request);

    InventoryResponse increaseStock(StockUpdateRequest request);

    List<StockCheckResponse> checkStock(List<StockCheckRequest> requests);

    void reserveStockForOrder(OrderCreatedEvent event);

    void rollbackStockForOrder(OrderCancelledEvent event);
}
