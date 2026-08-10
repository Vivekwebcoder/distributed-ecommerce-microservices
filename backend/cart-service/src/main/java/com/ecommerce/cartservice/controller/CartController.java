package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.AddToCartRequest;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;
import com.ecommerce.cartservice.security.UserPrincipal;
import com.ecommerce.cartservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Validated
@Tag(name = "Cart Management", description = "REST APIs for managing shopping cart items, quantities, and totals with JWT Security")
@SecurityRequirement(name = "Bearer Authentication")
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    @Operation(summary = "Add Item to Cart", description = "Adds a product to the authenticated user's shopping cart or updates quantity if item already exists.")
    public ResponseEntity<CartResponse> addItem(
            @Parameter(description = "Optional User ID passed from API Gateway via HTTP Header")
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @Valid @RequestBody AddToCartRequest request) {
        Long userId = resolveUserId(headerUserId);
        CartResponse response = cartService.addItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get User Shopping Cart", description = "Retrieves current active shopping cart details for the authenticated user.")
    public ResponseEntity<CartResponse> getCart(
            @Parameter(description = "Optional User ID passed from API Gateway via HTTP Header")
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {
        Long userId = resolveUserId(headerUserId);
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{productId}")
    @Operation(summary = "Update Cart Item Quantity", description = "Updates quantity of a specific product item in the user's shopping cart.")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @Parameter(description = "Optional User ID passed from API Gateway via HTTP Header")
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @PathVariable("productId") String productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        Long userId = resolveUserId(headerUserId);
        CartResponse response = cartService.updateItemQuantity(userId, productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove Item from Cart", description = "Removes a specific product item from the authenticated user's shopping cart.")
    public ResponseEntity<CartResponse> removeItem(
            @Parameter(description = "Optional User ID passed from API Gateway via HTTP Header")
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @PathVariable("productId") String productId) {
        Long userId = resolveUserId(headerUserId);
        CartResponse response = cartService.removeItem(userId, productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear User Cart", description = "Clears all items from the authenticated user's shopping cart (e.g. post-checkout).")
    public ResponseEntity<Void> clearCart(
            @Parameter(description = "Optional User ID passed from API Gateway via HTTP Header")
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {
        Long userId = resolveUserId(headerUserId);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    private Long resolveUserId(Long headerUserId) {
        if (headerUserId != null) {
            return headerUserId;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            if (principal.getUserId() != null) {
                try {
                    return Long.valueOf(principal.getUserId());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("User ID format invalid: " + principal.getUserId());
                }
            }
        }
        throw new IllegalArgumentException("User identity could not be resolved from JWT Bearer token or X-User-Id header");
    }
}
