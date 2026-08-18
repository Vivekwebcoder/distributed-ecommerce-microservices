package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.request.CancelOrderRequest;
import com.ecommerce.orderservice.dto.request.OrderCheckoutRequest;
import com.ecommerce.orderservice.dto.response.ApiResponse;
import com.ecommerce.orderservice.dto.response.OrderResponse;
import com.ecommerce.orderservice.dto.response.OrderStatusResponse;
import com.ecommerce.orderservice.dto.response.PageResponse;
import com.ecommerce.orderservice.security.UserPrincipal;
import com.ecommerce.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Endpoints for checkout, order details, cancellation, and history tracking")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @Operation(summary = "Place Order (Checkout)", description = "Accepts cart items or direct checkout payload, persists order in CREATED state, and publishes OrderCreatedEvent to Kafka.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(
            @Valid @RequestBody OrderCheckoutRequest checkoutRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = extractUserId(userPrincipal);
        log.info("REST request to checkout order for userId: {}", userId);

        OrderResponse response = orderService.checkoutOrder(userId, checkoutRequest);
        ApiResponse<OrderResponse> apiResponse = ApiResponse.success(
                "Order placed successfully. Processing payment and stock.",
                response
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{trackingNumber}")
    @Operation(summary = "Get Order Details by Tracking Number", description = "Fetches details of a single order by unique tracking number. Validates tenant access.")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByTrackingNumber(
            @Parameter(description = "Unique UUID-based tracking identifier") @PathVariable String trackingNumber,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = extractUserId(userPrincipal);
        boolean isAdmin = checkIsAdmin(userPrincipal);
        log.info("REST request to get order by trackingNumber: {}, userId: {}, isAdmin: {}", trackingNumber, userId, isAdmin);

        OrderResponse response = orderService.getOrderByTrackingNumber(trackingNumber, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/history")
    @Operation(summary = "Get User Order History (Paginated)", description = "Retrieves all historical orders placed by the currently authenticated user.")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getUserOrderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = extractUserId(userPrincipal);
        log.info("REST request to get order history for userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = createPageable(page, size, sort);
        PageResponse<OrderResponse> pageResponse = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @PutMapping("/{trackingNumber}/cancel")
    @Operation(summary = "Cancel Order", description = "Cancels an existing order if in cancellable state. Triggers Kafka compensation event.")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable String trackingNumber,
            @Valid @RequestBody CancelOrderRequest cancelRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = extractUserId(userPrincipal);
        boolean isAdmin = checkIsAdmin(userPrincipal);
        log.info("REST request to cancel order trackingNumber: {}, reason: {}", trackingNumber, cancelRequest.getReason());

        OrderResponse response = orderService.cancelOrder(trackingNumber, cancelRequest.getReason(), userId, isAdmin);
        ApiResponse<OrderResponse> apiResponse = ApiResponse.success(
                "Order cancellation initiated. Rollback compensation triggered.",
                response
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{trackingNumber}/status")
    @Operation(summary = "Track Order Status (Lightweight)", description = "Returns real-time execution status of an order for lightweight polling.")
    public ResponseEntity<ApiResponse<OrderStatusResponse>> getOrderStatus(
            @PathVariable String trackingNumber,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = extractUserId(userPrincipal);
        boolean isAdmin = checkIsAdmin(userPrincipal);
        log.info("REST request to get order status for trackingNumber: {}", trackingNumber);

        OrderStatusResponse statusResponse = orderService.getOrderStatus(trackingNumber, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(statusResponse));
    }

    private Long extractUserId(UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getUserId() == null) {
            return 101L; // Default tenant fallback for local development tests
        }
        try {
            return Long.parseLong(userPrincipal.getUserId());
        } catch (NumberFormatException e) {
            return 101L;
        }
    }

    private boolean checkIsAdmin(UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getAuthorities() == null) {
            return false;
        }
        return userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParts = sort.split(",");
        String property = sortParts[0];
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, property));
    }
}
