package com.ecommerce.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdateRequest {

    @NotBlank(message = "SKU Code is required")
    private String skuCode;

    @NotNull(message = "Quantity delta is required")
    @Min(value = 1, message = "Quantity delta must be at least 1")
    private Integer quantity;

    private String referenceId;
}
