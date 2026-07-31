package com.microservices.payment.kafka;

import com.microservices.payment.dto.event.PaymentInitiatedEvent;
import com.microservices.payment.dto.event.RefundInitiatedEvent;
import com.microservices.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${kafka.topics.payment-initiated:PaymentInitiated}", groupId = "${spring.kafka.consumer.group-id:payment-service-group}")
    public void consumePaymentInitiated(PaymentInitiatedEvent event) {
        log.info("Received PaymentInitiated Kafka event for orderId: {}", event.getOrderId());
        try {
            paymentService.processPaymentFromEvent(event);
        } catch (Exception e) {
            log.error("Error processing PaymentInitiated Kafka event for orderId: {}", event.getOrderId(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topics.refund-initiated:RefundInitiated}", groupId = "${spring.kafka.consumer.group-id:payment-service-group}")
    public void consumeRefundInitiated(RefundInitiatedEvent event) {
        log.info("Received RefundInitiated Kafka event for orderId: {}", event.getOrderId());
        try {
            paymentService.processRefundFromEvent(event);
        } catch (Exception e) {
            log.error("Error processing RefundInitiated Kafka event for orderId: {}", event.getOrderId(), e);
        }
    }
}
