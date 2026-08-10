package com.ecommerce.cartservice.service.impl;

import com.ecommerce.cartservice.client.ProductServiceClient;
import com.ecommerce.cartservice.dto.AddToCartRequest;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.ProductDto;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.exception.CartNotFoundException;
import com.ecommerce.cartservice.exception.InsufficientStockException;
import com.ecommerce.cartservice.exception.ProductNotFoundException;
import com.ecommerce.cartservice.mapper.CartMapper;
import com.ecommerce.cartservice.repository.CartItemRepository;
import com.ecommerce.cartservice.repository.CartRepository;
import com.ecommerce.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final CartMapper cartMapper;

    @Override
    public CartResponse addItem(Long userId, AddToCartRequest request) {
        log.info("Adding product {} to cart for user {}", request.productId(), userId);

        ProductDto product = fetchAndValidateProduct(request.productId());
        validateStock(product, request.quantity());

        Cart cart = getOrCreateCart(userId);

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.productId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + request.quantity();
            validateStock(product, newQuantity);

            existingItem.setQuantity(newQuantity);
            existingItem.setUnitPrice(product.price());
            existingItem.setProductName(product.name());
            existingItem.recalculateSubtotal();
            log.info("Updated item {} quantity to {}", request.productId(), newQuantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(product.id())
                    .productName(product.name())
                    .unitPrice(product.price() != null ? product.price() : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                    .quantity(request.quantity())
                    .build();
            newItem.recalculateSubtotal();
            cart.addItem(newItem);
            log.info("Added new item {} to cart", request.productId());
        }

        cart.recalculateTotals();
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponse(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        log.info("Retrieving cart for user {}", userId);
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> createEmptyCart(userId));
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse updateItemQuantity(Long userId, String productId, UpdateCartItemRequest request) {
        log.info("Updating product {} quantity to {} in cart for user {}", productId, request.quantity(), userId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product " + productId + " is not present in user's cart"));

        ProductDto product = fetchAndValidateProduct(productId);
        validateStock(product, request.quantity());

        item.setQuantity(request.quantity());
        item.setUnitPrice(product.price());
        item.setProductName(product.name());
        item.recalculateSubtotal();

        cart.recalculateTotals();
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponse(savedCart);
    }

    @Override
    public CartResponse removeItem(Long userId, String productId) {
        log.info("Removing product {} from cart for user {}", productId, userId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product " + productId + " is not present in user's cart"));

        cart.removeItem(item);
        cart.recalculateTotals();

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponse(savedCart);
    }

    @Override
    public void clearCart(Long userId) {
        log.info("Clearing cart for user {}", userId);

        Optional<Cart> cartOpt = cartRepository.findByUserIdWithItems(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.clearItems();
            cart.recalculateTotals();
            cartRepository.save(cart);
        }
    }

    private ProductDto fetchAndValidateProduct(String productId) {
        ProductDto product;
        try {
            product = productServiceClient.getProductById(productId);
        } catch (Exception e) {
            log.warn("Error calling ProductServiceClient for productId {}: {}", productId, e.getMessage());
            throw new ProductNotFoundException("Product with ID " + productId + " could not be found or verified.");
        }

        if (product == null || product.id() == null) {
            throw new ProductNotFoundException("Product with ID " + productId + " does not exist.");
        }
        return product;
    }

    private void validateStock(ProductDto product, int requestedQuantity) {
        if (product.stockQuantity() != null && product.stockQuantity() < requestedQuantity) {
            throw new InsufficientStockException("Insufficient stock for product '" + product.name()
                    + "'. Requested: " + requestedQuantity + ", Available: " + product.stockQuantity());
        }
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> createEmptyCart(userId));
    }

    private Cart createEmptyCart(Long userId) {
        Cart newCart = Cart.builder()
                .userId(userId)
                .totalAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                .totalItems(0)
                .build();
        return cartRepository.save(newCart);
    }
}
