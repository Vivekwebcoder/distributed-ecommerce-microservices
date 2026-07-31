package com.microservices.payment.dto;

import com.microservices.payment.domain.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @NotBlank(message = "OrderId is required")
    private String orderId;

    @NotBlank(message = "UserId is required")
    private String userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String currency = "INR";

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
