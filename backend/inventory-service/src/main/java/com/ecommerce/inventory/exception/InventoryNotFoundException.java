package com.ecommerce.inventory.exception;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String message) {
        super(message);
    }

    public InventoryNotFoundException(String skuCode, boolean isSku) {
        super("Inventory not found for SKU: " + skuCode);
    }
}
