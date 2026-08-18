package com.ecommerce.orderservice.kafka.producer;

import com.ecommerce.orderservice.config.KafkaConfig;
import com.ecommerce.orderservice.kafka.event.OrderCancelledEvent;
import com.ecommerce.orderservice.kafka.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Publishing OrderCreatedEvent for tracking number: {}", event.getOrderTrackingNumber());
        kafkaTemplate.send(KafkaConfig.TOPIC_ORDER_CREATED, event.getOrderTrackingNumber(), event);
    }

    public void sendOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Publishing OrderCancelledEvent for tracking number: {}, reason: {}", event.getOrderTrackingNumber(), event.getReason());
        kafkaTemplate.send(KafkaConfig.TOPIC_ORDER_CANCELLED, event.getOrderTrackingNumber(), event);
    }
}
