package com.microservices.payment.service.impl;

import com.microservices.payment.domain.Payment;
import com.microservices.payment.domain.PaymentMethod;
import com.microservices.payment.domain.PaymentStatus;
import com.microservices.payment.dto.*;
import com.microservices.payment.dto.event.PaymentFailedEvent;
import com.microservices.payment.dto.event.PaymentInitiatedEvent;
import com.microservices.payment.dto.event.PaymentSuccessEvent;
import com.microservices.payment.dto.event.RefundCompletedEvent;
import com.microservices.payment.dto.event.RefundInitiatedEvent;
import com.microservices.payment.exception.PaymentNotFoundException;
import com.microservices.payment.exception.PaymentProcessingException;
import com.microservices.payment.kafka.PaymentEventProducer;
import com.microservices.payment.repository.PaymentRepository;
import com.microservices.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @Override
    @Transactional
    public PaymentResponseDto processPayment(PaymentRequestDto requestDto) {
        log.info("Processing payment for Order ID: {}, Amount: {}", requestDto.getOrderId(), requestDto.getAmount());

        // Generate unique transaction ID
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();

        // 1. Create initial Pending Payment record
        Payment payment = Payment.builder()
                .orderId(requestDto.getOrderId())
                .userId(requestDto.getUserId())
                .amount(requestDto.getAmount())
                .currency(requestDto.getCurrency() != null ? requestDto.getCurrency() : "INR")
                .paymentMethod(requestDto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(transactionId)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // 2. Execute Payment Gateway Simulation
        boolean isGatewaySuccess = simulateGatewayTransaction(requestDto.getAmount());

        if (isGatewaySuccess) {
            savedPayment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(savedPayment);
            log.info("Payment SUCCESS for Order ID: {}, Transaction ID: {}", savedPayment.getOrderId(), transactionId);

            // Publish PaymentSuccess Event for Saga flow
            PaymentSuccessEvent successEvent = PaymentSuccessEvent.builder()
                    .orderId(savedPayment.getOrderId())
                    .userId(savedPayment.getUserId())
                    .paymentId(savedPayment.getId())
                    .transactionId(savedPayment.getTransactionId())
                    .amount(savedPayment.getAmount())
                    .status(PaymentStatus.SUCCESS.name())
                    .build();
            paymentEventProducer.sendPaymentSuccessEvent(successEvent);
        } else {
            savedPayment.setPaymentStatus(PaymentStatus.FAILED);
            savedPayment.setFailureReason("Insufficient funds or payment gateway declined");
            paymentRepository.save(savedPayment);
            log.warn("Payment FAILED for Order ID: {}, Transaction ID: {}", savedPayment.getOrderId(), transactionId);

            // Publish PaymentFailed Event for Saga Rollback
            PaymentFailedEvent failedEvent = PaymentFailedEvent.builder()
                    .orderId(savedPayment.getOrderId())
                    .userId(savedPayment.getUserId())
                    .paymentId(savedPayment.getId())
                    .amount(savedPayment.getAmount())
                    .reason(savedPayment.getFailureReason())
                    .build();
            paymentEventProducer.sendPaymentFailedEvent(failedEvent);
        }

        return mapToResponseDto(savedPayment);
    }

    @Override
    @Transactional
    public void processPaymentFromEvent(PaymentInitiatedEvent event) {
        log.info("Received PaymentInitiated event for order: {}", event.getOrderId());
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(event.getPaymentMethod().toUpperCase());
        } catch (Exception e) {
            method = PaymentMethod.CREDIT_CARD;
        }

        PaymentRequestDto requestDto = PaymentRequestDto.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .amount(event.getAmount())
                .currency("INR")
                .paymentMethod(method)
                .build();

        processPayment(requestDto);
    }

    @Override
    @Transactional
    public RefundResponseDto processRefund(RefundRequestDto requestDto) {
        log.info("Processing refund for orderId: {}", requestDto.getOrderId());

        Payment payment;
        if (requestDto.getTransactionId() != null && !requestDto.getTransactionId().isBlank()) {
            payment = paymentRepository.findByTransactionId(requestDto.getTransactionId())
                    .orElseThrow(() -> new PaymentNotFoundException("Payment not found for transactionId: " + requestDto.getTransactionId()));
        } else {
            payment = paymentRepository.findByOrderId(requestDto.getOrderId())
                    .orElseThrow(() -> new PaymentNotFoundException("Payment not found for orderId: " + requestDto.getOrderId()));
        }

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentProcessingException("Cannot refund a payment that is not in SUCCESS status. Current status: " + payment.getPaymentStatus());
        }

        String refundTxnId = "REF-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        // Publish RefundCompleted Event
        RefundCompletedEvent completedEvent = RefundCompletedEvent.builder()
                .orderId(payment.getOrderId())
                .transactionId(payment.getTransactionId())
                .refundTransactionId(refundTxnId)
                .amount(requestDto.getAmount() != null ? requestDto.getAmount() : payment.getAmount())
                .status(PaymentStatus.REFUNDED.name())
                .build();

        paymentEventProducer.sendRefundCompletedEvent(completedEvent);

        return RefundResponseDto.builder()
                .orderId(payment.getOrderId())
                .transactionId(payment.getTransactionId())
                .refundTransactionId(refundTxnId)
                .refundAmount(requestDto.getAmount() != null ? requestDto.getAmount() : payment.getAmount())
                .status(PaymentStatus.REFUNDED)
                .reason(requestDto.getReason() != null ? requestDto.getReason() : "Customer requested refund")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public void processRefundFromEvent(RefundInitiatedEvent event) {
        log.info("Received RefundInitiated event for order: {}", event.getOrderId());
        RefundRequestDto refundRequestDto = RefundRequestDto.builder()
                .orderId(event.getOrderId())
                .transactionId(event.getTransactionId())
                .amount(event.getAmount())
                .reason(event.getReason())
                .build();
        processRefund(refundRequestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentStatusDto getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));
        return mapToStatusDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentStatusDto getPaymentStatusByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for orderId: " + orderId));
        return mapToStatusDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentHistory(String userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getPaymentHistoryPaged(String userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable)
                .map(this::mapToResponseDto);
    }

    private boolean simulateGatewayTransaction(BigDecimal amount) {
        // Special test amount trigger for simulated failure testing: 9999 or negative
        if (amount != null && amount.compareTo(new BigDecimal("9999")) == 0) {
            return false;
        }
        return true;
    }

    private PaymentResponseDto mapToResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .failureReason(payment.getFailureReason())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    private PaymentStatusDto mapToStatusDto(Payment payment) {
        return PaymentStatusDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .transactionId(payment.getTransactionId())
                .status(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .failureReason(payment.getFailureReason())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
