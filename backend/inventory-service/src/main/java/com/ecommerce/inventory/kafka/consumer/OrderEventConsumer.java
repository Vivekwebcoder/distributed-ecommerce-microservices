package com.ecommerce.inventory.kafka.consumer;

import com.ecommerce.inventory.dto.event.OrderCancelledEvent;
import com.ecommerce.inventory.dto.event.OrderCreatedEvent;
import com.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-created-topic", groupId = "inventory-group")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for Order ID: {}", event.getOrderId());
        try {
            inventoryService.reserveStockForOrder(event);
        } catch (Exception ex) {
            log.error("Error processing OrderCreatedEvent for Order ID: {}: {}", event.getOrderId(), ex.getMessage());
        }
    }

    @KafkaListener(topics = {"order-cancelled-topic", "payment-failed-topic"}, groupId = "inventory-group")
    public void consumeOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Received OrderCancelledEvent/PaymentFailedEvent for Order ID: {}", event.getOrderId());
        try {
            inventoryService.rollbackStockForOrder(event);
        } catch (Exception ex) {
            log.error("Error processing OrderCancelledEvent for Order ID: {}: {}", event.getOrderId(), ex.getMessage());
        }
    }
}
