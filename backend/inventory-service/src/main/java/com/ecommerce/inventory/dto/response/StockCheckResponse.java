package com.ecommerce.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCheckResponse {

    private String skuCode;
    private Integer requestedQuantity;
    private Integer availableQuantity;
    private boolean isAvailable;
}
