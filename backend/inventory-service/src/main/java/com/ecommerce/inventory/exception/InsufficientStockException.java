package com.ecommerce.inventory.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String skuCode, int requested, int available) {
        super(String.format("Insufficient stock for SKU: %s. Requested: %d, Available: %d", skuCode, requested, available));
    }
}
