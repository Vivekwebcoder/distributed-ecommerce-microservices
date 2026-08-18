package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.client.CartFeignClient;
import com.ecommerce.orderservice.dto.request.OrderCheckoutRequest;
import com.ecommerce.orderservice.dto.request.OrderItemRequest;
import com.ecommerce.orderservice.dto.response.OrderResponse;
import com.ecommerce.orderservice.dto.response.OrderStatusResponse;
import com.ecommerce.orderservice.dto.response.PageResponse;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.exception.InvalidOrderStateException;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.exception.UnauthorizedAccessException;
import com.ecommerce.orderservice.kafka.event.OrderCancelledEvent;
import com.ecommerce.orderservice.kafka.event.OrderCreatedEvent;
import com.ecommerce.orderservice.kafka.event.OrderItemEventDto;
import com.ecommerce.orderservice.kafka.producer.OrderEventProducer;
import com.ecommerce.orderservice.mapper.OrderMapper;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;
    private final CartFeignClient cartFeignClient;

    @Override
    public OrderResponse checkoutOrder(Long userId, OrderCheckoutRequest checkoutRequest) {
        log.info("Initiating order checkout for userId: {}", userId);

        String trackingNumber = generateOrderTrackingNumber();
        String shippingAddressJson = orderMapper.addressDtoToString(checkoutRequest.getShippingAddress());
        String billingAddressJson = orderMapper.addressDtoToString(checkoutRequest.getBillingAddress());

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalQuantity = 0;

        Order order = Order.builder()
                .orderTrackingNumber(trackingNumber)
                .userId(userId)
                .orderStatus(OrderStatus.CREATED)
                .paymentMode(checkoutRequest.getPaymentMode())
                .shippingAddress(shippingAddressJson)
                .billingAddress(billingAddressJson)
                .build();

        for (OrderItemRequest itemRequest : checkoutRequest.getItems()) {
            BigDecimal subtotal = itemRequest.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            totalQuantity += itemRequest.getQuantity();

            OrderItem item = OrderItem.builder()
                    .productId(itemRequest.getProductId())
                    .productSku(itemRequest.getProductSku())
                    .productName(itemRequest.getProductName())
                    .unitPrice(itemRequest.getUnitPrice())
                    .quantity(itemRequest.getQuantity())
                    .subtotal(subtotal)
                    .build();

            order.addOrderItem(item);
        }

        order.setTotalAmount(totalAmount);
        order.setTotalQuantity(totalQuantity);

        Order savedOrder = orderRepository.save(order);
        log.info("Saved order with ID: {} and tracking number: {}", savedOrder.getId(), savedOrder.getOrderTrackingNumber());

        // Publish OrderCreatedEvent for Kafka Saga Pattern
        publishOrderCreatedEvent(savedOrder);

        // Async clear cart fallback
        try {
            cartFeignClient.clearCart(String.valueOf(userId));
        } catch (Exception ex) {
            log.warn("Failed to clear cart via Feign for userId {}: {}", userId, ex.getMessage());
        }

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByTrackingNumber(String trackingNumber, Long userId, boolean isAdmin) {
        Order order = findOrder(trackingNumber);
        validateTenantAccess(order, userId, isAdmin);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
        Page<OrderResponse> responsePage = orderPage.map(orderMapper::toOrderResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    public OrderResponse cancelOrder(String trackingNumber, String reason, Long userId, boolean isAdmin) {
        Order order = findOrder(trackingNumber);
        validateTenantAccess(order, userId, isAdmin);

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Order is already cancelled");
        }
        if (order.getOrderStatus() == OrderStatus.DELIVERED || order.getOrderStatus() == OrderStatus.REFUNDED) {
            throw new InvalidOrderStateException("Cannot cancel order in state: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(reason);
        Order updatedOrder = orderRepository.save(order);
        log.info("Order {} status updated to CANCELLED. Reason: {}", trackingNumber, reason);

        // Publish OrderCancelledEvent for compensation rollback
        publishOrderCancelledEvent(updatedOrder, reason);

        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatusResponse getOrderStatus(String trackingNumber, Long userId, boolean isAdmin) {
        Order order = findOrder(trackingNumber);
        validateTenantAccess(order, userId, isAdmin);
        return orderMapper.toOrderStatusResponse(order);
    }

    @Override
    public void processInventoryReserved(String trackingNumber) {
        log.info("Saga Callback: Inventory reserved for order {}", trackingNumber);
        orderRepository.findByOrderTrackingNumber(trackingNumber).ifPresent(order -> {
            if (order.getOrderStatus() == OrderStatus.CREATED) {
                order.setOrderStatus(OrderStatus.INVENTORY_RESERVED);
                orderRepository.save(order);
                log.info("Order {} status flipped to INVENTORY_RESERVED", trackingNumber);
            }
        });
    }

    @Override
    public void processInventoryFailed(String trackingNumber, String reason) {
        log.warn("Saga Callback: Inventory reservation failed for order {}: {}", trackingNumber, reason);
        orderRepository.findByOrderTrackingNumber(trackingNumber).ifPresent(order -> {
            if (order.getOrderStatus() != OrderStatus.CANCELLED) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                order.setCancellationReason("Inventory failed: " + reason);
                Order cancelledOrder = orderRepository.save(order);
                publishOrderCancelledEvent(cancelledOrder, "Inventory reservation failed: " + reason);
            }
        });
    }

    @Override
    public void processPaymentCompleted(String trackingNumber, String paymentId) {
        log.info("Saga Callback: Payment completed for order {}, paymentId: {}", trackingNumber, paymentId);
        orderRepository.findByOrderTrackingNumber(trackingNumber).ifPresent(order -> {
            order.setOrderStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
            log.info("Order {} status updated to CONFIRMED", trackingNumber);
        });
    }

    @Override
    public void processPaymentFailed(String trackingNumber, String reason) {
        log.warn("Saga Callback: Payment failed for order {}: {}", trackingNumber, reason);
        orderRepository.findByOrderTrackingNumber(trackingNumber).ifPresent(order -> {
            if (order.getOrderStatus() != OrderStatus.CANCELLED) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                order.setCancellationReason("Payment failed: " + reason);
                Order cancelledOrder = orderRepository.save(order);
                publishOrderCancelledEvent(cancelledOrder, "Payment failed: " + reason);
            }
        });
    }

    private Order findOrder(String trackingNumber) {
        return orderRepository.findByOrderTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order with tracking number " + trackingNumber + " not found"));
    }

    private void validateTenantAccess(Order order, Long userId, boolean isAdmin) {
        if (!isAdmin && !order.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("Access denied. You do not own order tracking number: " + order.getOrderTrackingNumber());
        }
    }

    private String generateOrderTrackingNumber() {
        String uuidSegment = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "ORD-2026-" + uuidSegment;
    }

    private void publishOrderCreatedEvent(Order order) {
        List<OrderItemEventDto> eventItems = order.getOrderItems().stream()
                .map(item -> OrderItemEventDto.builder()
                        .productId(item.getProductId())
                        .skuCode(item.getProductSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .collect(Collectors.toList());

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .paymentMode(order.getPaymentMode())
                .items(eventItems)
                .shippingAddress(order.getShippingAddress())
                .build();

        orderEventProducer.sendOrderCreatedEvent(event);
    }

    private void publishOrderCancelledEvent(Order order, String reason) {
        List<OrderItemEventDto> eventItems = order.getOrderItems().stream()
                .map(item -> OrderItemEventDto.builder()
                        .productId(item.getProductId())
                        .skuCode(item.getProductSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .collect(Collectors.toList());

        OrderCancelledEvent event = OrderCancelledEvent.builder()
                .orderId(order.getId())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .userId(order.getUserId())
                .reason(reason)
                .items(eventItems)
                .build();

        orderEventProducer.sendOrderCancelledEvent(event);
    }
}
