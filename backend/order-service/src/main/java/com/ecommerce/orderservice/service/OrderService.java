package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.request.OrderCheckoutRequest;
import com.ecommerce.orderservice.dto.response.OrderResponse;
import com.ecommerce.orderservice.dto.response.OrderStatusResponse;
import com.ecommerce.orderservice.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse checkoutOrder(Long userId, OrderCheckoutRequest checkoutRequest);

    OrderResponse getOrderByTrackingNumber(String trackingNumber, Long userId, boolean isAdmin);

    PageResponse<OrderResponse> getUserOrders(Long userId, Pageable pageable);

    OrderResponse cancelOrder(String trackingNumber, String reason, Long userId, boolean isAdmin);

    OrderStatusResponse getOrderStatus(String trackingNumber, Long userId, boolean isAdmin);

    void processInventoryReserved(String trackingNumber);

    void processInventoryFailed(String trackingNumber, String reason);

    void processPaymentCompleted(String trackingNumber, String paymentId);

    void processPaymentFailed(String trackingNumber, String reason);
}
