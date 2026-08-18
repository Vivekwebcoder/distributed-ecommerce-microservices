package com.ecommerce.orderservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String orderTrackingNumber;
    private Long userId;
    private BigDecimal totalAmount;
    private String paymentMode;
    private List<OrderItemEventDto> items;
    private String shippingAddress;
}
