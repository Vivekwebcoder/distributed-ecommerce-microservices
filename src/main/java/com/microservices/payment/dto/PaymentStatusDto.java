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
public class PaymentStatusDto {
    private Long paymentId;
    private String orderId;
    private String transactionId;
    private PaymentStatus status;
    private BigDecimal amount;
    private String failureReason;
    private LocalDateTime updatedAt;
}
