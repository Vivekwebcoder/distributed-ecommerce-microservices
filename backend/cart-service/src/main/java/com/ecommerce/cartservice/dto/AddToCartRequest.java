package com.ecommerce.cartservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
    @NotNull(message = "Product ID is mandatory")
    String productId,

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity cannot exceed 100 per item")
    Integer quantity
) {}
