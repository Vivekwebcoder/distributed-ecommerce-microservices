package com.ecommerce.inventory.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent {

    private String orderId;
    private String userId;
    private List<OrderItemDto> items;
    private BigDecimal totalAmount;
    private String status;
}
