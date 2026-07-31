package com.microservices.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topics.payment-success:PaymentSuccess}")
    private String paymentSuccessTopic;

    @Value("${kafka.topics.payment-failed:PaymentFailed}")
    private String paymentFailedTopic;

    @Value("${kafka.topics.refund-completed:RefundCompleted}")
    private String refundCompletedTopic;

    @Bean
    public NewTopic paymentSuccessTopic() {
        return TopicBuilder.name(paymentSuccessTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentFailedTopic() {
        return TopicBuilder.name(paymentFailedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic refundCompletedTopic() {
        return TopicBuilder.name(refundCompletedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
