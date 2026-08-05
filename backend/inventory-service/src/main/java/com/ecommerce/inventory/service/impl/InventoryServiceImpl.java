package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.dto.event.InventoryFailedEvent;
import com.ecommerce.inventory.dto.event.InventoryReservedEvent;
import com.ecommerce.inventory.dto.event.OrderCancelledEvent;
import com.ecommerce.inventory.dto.event.OrderCreatedEvent;
import com.ecommerce.inventory.dto.event.OrderItemDto;
import com.ecommerce.inventory.dto.request.InventoryCreateRequest;
import com.ecommerce.inventory.dto.request.StockCheckRequest;
import com.ecommerce.inventory.dto.request.StockUpdateRequest;
import com.ecommerce.inventory.dto.response.InventoryResponse;
import com.ecommerce.inventory.dto.response.StockCheckResponse;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.entity.InventoryAuditLog;
import com.ecommerce.inventory.enums.AuditAction;
import com.ecommerce.inventory.exception.DuplicateSkuException;
import com.ecommerce.inventory.exception.InsufficientStockException;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import com.ecommerce.inventory.kafka.producer.InventoryEventProducer;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryAuditLogRepository;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.InventoryService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryAuditLogRepository auditLogRepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryEventProducer inventoryEventProducer;

    @Override
    @Transactional
    public InventoryResponse createInventory(InventoryCreateRequest request) {
        log.info("Creating inventory entry for SKU: {}", request.getSkuCode());
        if (inventoryRepository.existsBySkuCode(request.getSkuCode())) {
            throw new DuplicateSkuException(request.getSkuCode(), true);
        }

        Inventory inventory = inventoryMapper.toEntity(request);
        inventory.recalculateStatus();
        Inventory savedInventory = inventoryRepository.save(inventory);

        createAuditLog(savedInventory.getSkuCode(), AuditAction.STOCK_ADDED, savedInventory.getQuantity(), "INITIAL_CREATION");

        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryBySku(String skuCode) {
        log.info("Fetching inventory for SKU: {}", skuCode);
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException(skuCode, true));
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    @Retry(name = "inventoryServiceRetry")
    public InventoryResponse updateStock(String skuCode, StockUpdateRequest request) {
        log.info("Updating stock level for SKU: {} with target delta: {}", skuCode, request.getQuantity());
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException(skuCode, true));

        int oldQuantity = inventory.getQuantity();
        int newQuantity = request.getQuantity();
        int delta = newQuantity - oldQuantity;

        inventory.setQuantity(newQuantity);
        inventory.recalculateStatus();
        Inventory savedInventory = inventoryRepository.save(inventory);

        AuditAction action = delta >= 0 ? AuditAction.STOCK_ADDED : AuditAction.DEDUCTED;
        createAuditLog(skuCode, action, delta, request.getReferenceId());

        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional
    @Retry(name = "inventoryServiceRetry")
    public InventoryResponse reduceStock(StockUpdateRequest request) {
        log.info("Reducing stock for SKU: {} by quantity: {}", request.getSkuCode(), request.getQuantity());
        Inventory inventory = inventoryRepository.findBySkuCode(request.getSkuCode())
                .orElseThrow(() -> new InventoryNotFoundException(request.getSkuCode(), true));

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(request.getSkuCode(), request.getQuantity(), inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventory.recalculateStatus();
        Inventory savedInventory = inventoryRepository.save(inventory);

        createAuditLog(request.getSkuCode(), AuditAction.DEDUCTED, -request.getQuantity(), request.getReferenceId());

        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional
    @Retry(name = "inventoryServiceRetry")
    public InventoryResponse increaseStock(StockUpdateRequest request) {
        log.info("Increasing stock for SKU: {} by quantity: {}", request.getSkuCode(), request.getQuantity());
        Inventory inventory = inventoryRepository.findBySkuCode(request.getSkuCode())
                .orElseThrow(() -> new InventoryNotFoundException(request.getSkuCode(), true));

        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        inventory.recalculateStatus();
        Inventory savedInventory = inventoryRepository.save(inventory);

        createAuditLog(request.getSkuCode(), AuditAction.STOCK_ADDED, request.getQuantity(), request.getReferenceId());

        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockCheckResponse> checkStock(List<StockCheckRequest> requests) {
        log.info("Performing batch stock availability check for {} items", requests.size());
        List<StockCheckResponse> responses = new ArrayList<>();

        for (StockCheckRequest req : requests) {
            Inventory inventory = inventoryRepository.findBySkuCode(req.getSkuCode()).orElse(null);
            if (inventory == null) {
                responses.add(StockCheckResponse.builder()
                        .skuCode(req.getSkuCode())
                        .requestedQuantity(req.getQuantity())
                        .availableQuantity(0)
                        .isAvailable(false)
                        .build());
            } else {
                int available = inventory.getQuantity();
                boolean isAvailable = available >= req.getQuantity();
                responses.add(StockCheckResponse.builder()
                        .skuCode(req.getSkuCode())
                        .requestedQuantity(req.getQuantity())
                        .availableQuantity(available)
                        .isAvailable(isAvailable)
                        .build());
            }
        }

        return responses;
    }

    @Override
    @Transactional
    @Retry(name = "inventoryServiceRetry")
    public void reserveStockForOrder(OrderCreatedEvent event) {
        log.info("Attempting to reserve stock for Order ID: {}", event.getOrderId());
        try {
            // Verification step for all items
            for (OrderItemDto item : event.getItems()) {
                Inventory inventory = inventoryRepository.findBySkuCode(item.getSkuCode())
                        .orElseThrow(() -> new InventoryNotFoundException(item.getSkuCode(), true));

                if (inventory.getQuantity() < item.getQuantity()) {
                    throw new InsufficientStockException(item.getSkuCode(), item.getQuantity(), inventory.getQuantity());
                }
            }

            // Mutation step once all pass verification
            for (OrderItemDto item : event.getItems()) {
                Inventory inventory = inventoryRepository.findBySkuCode(item.getSkuCode()).get();
                inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
                inventory.setReservedQuantity(inventory.getReservedQuantity() + item.getQuantity());
                inventory.recalculateStatus();
                inventoryRepository.save(inventory);

                createAuditLog(item.getSkuCode(), AuditAction.RESERVED, item.getQuantity(), event.getOrderId());
            }

            // Emit success saga event
            InventoryReservedEvent reservedEvent = InventoryReservedEvent.builder()
                    .orderId(event.getOrderId())
                    .items(event.getItems())
                    .status("RESERVED")
                    .build();
            inventoryEventProducer.sendInventoryReservedEvent(reservedEvent);
            log.info("Successfully reserved stock for Order ID: {}", event.getOrderId());

        } catch (Exception ex) {
            log.error("Failed to reserve stock for Order ID: {}. Reason: {}", event.getOrderId(), ex.getMessage());
            InventoryFailedEvent failedEvent = InventoryFailedEvent.builder()
                    .orderId(event.getOrderId())
                    .reason(ex.getMessage())
                    .build();
            inventoryEventProducer.sendInventoryFailedEvent(failedEvent);
            throw ex; // Trigger transaction rollback
        }
    }

    @Override
    @Transactional
    @Retry(name = "inventoryServiceRetry")
    public void rollbackStockForOrder(OrderCancelledEvent event) {
        log.info("Rolling back stock reservation for cancelled Order ID: {}", event.getOrderId());
        if (event.getItems() == null || event.getItems().isEmpty()) {
            log.warn("No items found in OrderCancelledEvent for Order ID: {}", event.getOrderId());
            return;
        }

        for (OrderItemDto item : event.getItems()) {
            inventoryRepository.findBySkuCode(item.getSkuCode()).ifPresent(inventory -> {
                inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
                int updatedReserved = Math.max(0, inventory.getReservedQuantity() - item.getQuantity());
                inventory.setReservedQuantity(updatedReserved);
                inventory.recalculateStatus();
                inventoryRepository.save(inventory);

                createAuditLog(item.getSkuCode(), AuditAction.RELEASED, item.getQuantity(), event.getOrderId());
                log.info("Released {} units of SKU: {} for Order ID: {}", item.getQuantity(), item.getSkuCode(), event.getOrderId());
            });
        }
    }

    private void createAuditLog(String skuCode, AuditAction action, Integer quantityChanged, String referenceId) {
        InventoryAuditLog auditLog = InventoryAuditLog.builder()
                .skuCode(skuCode)
                .action(action)
                .quantityChanged(quantityChanged)
                .referenceId(referenceId)
                .build();
        auditLogRepository.save(auditLog);
    }
}
