package com.ecommerce.inventory.kafka.producer;

import com.ecommerce.inventory.dto.event.InventoryFailedEvent;
import com.ecommerce.inventory.dto.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventProducer {

    public static final String INVENTORY_RESERVED_TOPIC = "inventory-reserved-topic";
    public static final String INVENTORY_FAILED_TOPIC = "inventory-failed-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventoryReservedEvent(InventoryReservedEvent event) {
        log.info("Publishing InventoryReservedEvent to topic: {} for Order ID: {}", INVENTORY_RESERVED_TOPIC, event.getOrderId());
        kafkaTemplate.send(INVENTORY_RESERVED_TOPIC, event.getOrderId(), event);
    }

    public void sendInventoryFailedEvent(InventoryFailedEvent event) {
        log.info("Publishing InventoryFailedEvent to topic: {} for Order ID: {}", INVENTORY_FAILED_TOPIC, event.getOrderId());
        kafkaTemplate.send(INVENTORY_FAILED_TOPIC, event.getOrderId(), event);
    }
}
