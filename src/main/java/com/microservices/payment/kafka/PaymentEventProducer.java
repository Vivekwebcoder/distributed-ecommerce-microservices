package com.microservices.payment.kafka;

import com.microservices.payment.dto.event.PaymentFailedEvent;
import com.microservices.payment.dto.event.PaymentSuccessEvent;
import com.microservices.payment.dto.event.RefundCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.payment-success:PaymentSuccess}")
    private String paymentSuccessTopic;

    @Value("${kafka.topics.payment-failed:PaymentFailed}")
    private String paymentFailedTopic;

    @Value("${kafka.topics.refund-completed:RefundCompleted}")
    private String refundCompletedTopic;

    public void sendPaymentSuccessEvent(PaymentSuccessEvent event) {
        log.info("Publishing PaymentSuccess event for orderId: {}", event.getOrderId());
        kafkaTemplate.send(paymentSuccessTopic, event.getOrderId(), event);
    }

    public void sendPaymentFailedEvent(PaymentFailedEvent event) {
        log.info("Publishing PaymentFailed event for orderId: {}", event.getOrderId());
        kafkaTemplate.send(paymentFailedTopic, event.getOrderId(), event);
    }

    public void sendRefundCompletedEvent(RefundCompletedEvent event) {
        log.info("Publishing RefundCompleted event for orderId: {}", event.getOrderId());
        kafkaTemplate.send(refundCompletedTopic, event.getOrderId(), event);
    }
}
