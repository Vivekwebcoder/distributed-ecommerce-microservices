package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;

public record CartItemResponse(
    Long itemId,
    String productId,
    String productName,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal subtotal
) {}
