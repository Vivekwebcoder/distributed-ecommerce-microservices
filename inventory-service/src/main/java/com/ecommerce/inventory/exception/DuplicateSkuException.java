package com.ecommerce.inventory.exception;

public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String message) {
        super(message);
    }

    public DuplicateSkuException(String skuCode, boolean isSku) {
        super("Inventory item already exists for SKU: " + skuCode);
    }
}
