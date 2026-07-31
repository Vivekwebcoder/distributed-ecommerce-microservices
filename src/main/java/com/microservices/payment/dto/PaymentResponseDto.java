package com.microservices.payment.dto;

import com.microservices.payment.domain.PaymentMethod;
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
public class PaymentResponseDto {
    private Long id;
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
