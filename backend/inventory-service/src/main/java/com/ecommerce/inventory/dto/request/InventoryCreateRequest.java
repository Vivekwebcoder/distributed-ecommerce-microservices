package com.ecommerce.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryCreateRequest {

    @NotBlank(message = "SKU Code is required")
    @Size(max = 100, message = "SKU Code cannot exceed 100 characters")
    private String skuCode;

    @NotNull(message = "Initial quantity is required")
    @Min(value = 0, message = "Initial quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Reorder threshold cannot be negative")
    @Builder.Default
    private Integer reorderThreshold = 10;
}
