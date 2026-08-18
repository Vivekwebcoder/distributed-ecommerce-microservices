package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderTrackingNumber(String orderTrackingNumber);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Optional<Order> findByUserIdAndOrderTrackingNumber(Long userId, String orderTrackingNumber);
}
