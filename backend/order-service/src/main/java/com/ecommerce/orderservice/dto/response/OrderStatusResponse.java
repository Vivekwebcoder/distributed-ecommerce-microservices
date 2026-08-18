package com.ecommerce.orderservice.dto.response;

import com.ecommerce.orderservice.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStatusResponse {

    private String orderTrackingNumber;
    private OrderStatus status;
    private Instant lastUpdated;
}
