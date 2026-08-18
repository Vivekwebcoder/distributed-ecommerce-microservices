package com.ecommerce.orderservice.dto.response;

import com.ecommerce.orderservice.dto.request.AddressDto;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private String orderTrackingNumber;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer totalQuantity;
    private Integer totalItems;
    private OrderStatus orderStatus;
    private String paymentMode;
    private AddressDto shippingAddress;
    private AddressDto billingAddress;
    private String cancellationReason;
    private Instant createdAt;
    private Instant updatedAt;
    private List<OrderItemResponse> items;
}
