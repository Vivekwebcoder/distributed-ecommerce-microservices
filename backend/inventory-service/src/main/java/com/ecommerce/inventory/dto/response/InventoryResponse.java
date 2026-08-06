package com.ecommerce.inventory.dto.response;

import com.ecommerce.inventory.enums.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private Long id;
    private String skuCode;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer reorderThreshold;
    private StockStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
