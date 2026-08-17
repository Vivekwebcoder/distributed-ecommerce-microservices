package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
    Long cartId,
    Long userId,
    BigDecimal totalAmount,
    Integer totalItems,
    List<CartItemResponse> items
) {}
