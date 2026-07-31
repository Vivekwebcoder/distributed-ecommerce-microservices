package com.microservices.payment.repository;

import com.microservices.payment.domain.Payment;
import com.microservices.payment.domain.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByOrderId(String orderId);

    List<Payment> findByUserId(String userId);

    Page<Payment> findByUserId(String userId, Pageable pageable);

    List<Payment> findByUserIdAndPaymentStatus(String userId, PaymentStatus status);
}
