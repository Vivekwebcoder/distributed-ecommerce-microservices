package com.ecommerce.orderservice.entity;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    INVENTORY_RESERVED,
    CONFIRMED,
    CANCELLED,
    DELIVERED,
    REFUNDED
}
