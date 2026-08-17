package com.ecommerce.cartservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "cart",
    indexes = {
        @Index(name = "idx_cart_user_id", columnList = "user_id", unique = true)
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Column(name = "total_items", nullable = false)
    @Builder.Default
    private Integer totalItems = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.totalAmount == null) {
            this.totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (this.totalItems == null) {
            this.totalItems = 0;
        }
        recalculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        recalculateTotals();
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        recalculateTotals();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        recalculateTotals();
    }

    public void clearItems() {
        items.forEach(item -> item.setCart(null));
        items.clear();
        recalculateTotals();
    }

    public void recalculateTotals() {
        if (items == null || items.isEmpty()) {
            this.totalItems = 0;
            this.totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            return;
        }

        int count = 0;
        BigDecimal sum = BigDecimal.ZERO;

        for (CartItem item : items) {
            if (item != null) {
                item.recalculateSubtotal();
                count += item.getQuantity() != null ? item.getQuantity() : 0;
                if (item.getSubtotal() != null) {
                    sum = sum.add(item.getSubtotal());
                }
            }
        }

        this.totalItems = count;
        this.totalAmount = sum.setScale(2, RoundingMode.HALF_UP);
    }
}
