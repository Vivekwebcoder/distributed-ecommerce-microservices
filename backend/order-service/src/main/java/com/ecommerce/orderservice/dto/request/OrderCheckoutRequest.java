package com.ecommerce.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCheckoutRequest {

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressDto shippingAddress;

    @NotNull(message = "Billing address is required")
    @Valid
    private AddressDto billingAddress;

    @NotNull(message = "Payment mode is required")
    @Pattern(regexp = "^(UPI|CREDIT_CARD|DEBIT_CARD|NET_BANKING|COD)$", message = "Payment mode must be one of: UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING, COD")
    private String paymentMode;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;
}
