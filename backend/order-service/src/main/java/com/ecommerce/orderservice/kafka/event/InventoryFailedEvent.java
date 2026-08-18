package com.ecommerce.orderservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryFailedEvent {

    private String orderTrackingNumber;
    private Long userId;
    private String reason;
}
