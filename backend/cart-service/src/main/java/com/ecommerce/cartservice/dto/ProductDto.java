package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;

public record ProductDto(
    String id,
    String name,
    BigDecimal price,
    Integer stockQuantity
) {}
