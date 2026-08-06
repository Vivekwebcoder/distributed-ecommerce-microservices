package com.ecommerce.inventory.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservedEvent {

    private String orderId;
    private List<OrderItemDto> items;
    private String status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
