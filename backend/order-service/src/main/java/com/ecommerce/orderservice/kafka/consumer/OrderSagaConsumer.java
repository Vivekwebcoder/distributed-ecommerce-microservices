package com.ecommerce.orderservice.kafka.consumer;

import com.ecommerce.orderservice.kafka.event.InventoryFailedEvent;
import com.ecommerce.orderservice.kafka.event.InventoryReservedEvent;
import com.ecommerce.orderservice.kafka.event.PaymentCompletedEvent;
import com.ecommerce.orderservice.kafka.event.PaymentFailedEvent;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "inventory-reserved-events", groupId = "order-saga-group")
    public void consumeInventoryReserved(InventoryReservedEvent event) {
        log.info("Received InventoryReservedEvent for order: {}", event.getOrderTrackingNumber());
        try {
            orderService.processInventoryReserved(event.getOrderTrackingNumber());
        } catch (Exception ex) {
            log.error("Error processing InventoryReservedEvent for order {}: {}", event.getOrderTrackingNumber(), ex.getMessage());
        }
    }

    @KafkaListener(topics = "inventory-failed-events", groupId = "order-saga-group")
    public void consumeInventoryFailed(InventoryFailedEvent event) {
        log.warn("Received InventoryFailedEvent for order: {}, reason: {}", event.getOrderTrackingNumber(), event.getReason());
        try {
            orderService.processInventoryFailed(event.getOrderTrackingNumber(), event.getReason());
        } catch (Exception ex) {
            log.error("Error processing InventoryFailedEvent for order {}: {}", event.getOrderTrackingNumber(), ex.getMessage());
        }
    }

    @KafkaListener(topics = {"payment-success-events", "payment-completed-events"}, groupId = "order-saga-group")
    public void consumePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent for order: {}, paymentId: {}", event.getOrderTrackingNumber(), event.getPaymentId());
        try {
            orderService.processPaymentCompleted(event.getOrderTrackingNumber(), event.getPaymentId());
        } catch (Exception ex) {
            log.error("Error processing PaymentCompletedEvent for order {}: {}", event.getOrderTrackingNumber(), ex.getMessage());
        }
    }

    @KafkaListener(topics = "payment-failed-events", groupId = "order-saga-group")
    public void consumePaymentFailed(PaymentFailedEvent event) {
        log.warn("Received PaymentFailedEvent for order: {}, reason: {}", event.getOrderTrackingNumber(), event.getReason());
        try {
            orderService.processPaymentFailed(event.getOrderTrackingNumber(), event.getReason());
        } catch (Exception ex) {
            log.error("Error processing PaymentFailedEvent for order {}: {}", event.getOrderTrackingNumber(), ex.getMessage());
        }
    }
}
