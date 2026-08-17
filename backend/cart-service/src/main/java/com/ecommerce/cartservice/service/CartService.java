package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.dto.AddToCartRequest;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;

public interface CartService {

    CartResponse addItem(Long userId, AddToCartRequest request);

    CartResponse getCart(Long userId);

    CartResponse updateItemQuantity(Long userId, String productId, UpdateCartItemRequest request);

    CartResponse removeItem(Long userId, String productId);

    void clearCart(Long userId);
}
