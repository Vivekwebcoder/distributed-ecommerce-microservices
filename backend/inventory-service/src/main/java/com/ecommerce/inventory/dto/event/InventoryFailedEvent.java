package com.ecommerce.inventory.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryFailedEvent {

    private String orderId;
    private String reason;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
