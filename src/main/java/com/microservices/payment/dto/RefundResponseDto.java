package com.microservices.payment.dto;

import com.microservices.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponseDto {
    private String orderId;
    private String transactionId;
    private String refundTransactionId;
    private BigDecimal refundAmount;
    private PaymentStatus status;
    private String reason;
    private LocalDateTime timestamp;
}
