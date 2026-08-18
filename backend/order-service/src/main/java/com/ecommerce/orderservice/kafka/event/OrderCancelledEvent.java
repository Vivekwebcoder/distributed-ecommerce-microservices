package com.ecommerce.orderservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent {

    private Long orderId;
    private String orderTrackingNumber;
    private Long userId;
    private String reason;
    private List<OrderItemEventDto> items;
}
