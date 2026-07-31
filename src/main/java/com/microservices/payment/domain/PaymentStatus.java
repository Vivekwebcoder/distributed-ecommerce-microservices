package com.microservices.payment.domain;

public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUND_PENDING,
    REFUNDED
}
