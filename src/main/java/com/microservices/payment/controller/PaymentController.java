package com.microservices.payment.controller;

import com.microservices.payment.dto.*;
import com.microservices.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Roadmap API: Pay
     * Initiates and processes a money transfer/payment.
     */
    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processPayment(@Valid @RequestBody PaymentRequestDto requestDto) {
        log.info("REST Request: Process Payment for orderId: {}", requestDto.getOrderId());
        PaymentResponseDto response = paymentService.processPayment(requestDto);
        ApiResponse<PaymentResponseDto> apiResponse = ApiResponse.<PaymentResponseDto>builder()
                .success(response.getPaymentStatus() == com.microservices.payment.domain.PaymentStatus.SUCCESS)
                .message("Payment processed with status: " + response.getPaymentStatus())
                .data(response)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    /**
     * Roadmap API: Refund
     * Initiates refund compensation flow for a transaction/order.
     */
    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<RefundResponseDto>> processRefund(@Valid @RequestBody RefundRequestDto requestDto) {
        log.info("REST Request: Refund for orderId: {}", requestDto.getOrderId());
        RefundResponseDto response = paymentService.processRefund(requestDto);
        ApiResponse<RefundResponseDto> apiResponse = ApiResponse.<RefundResponseDto>builder()
                .success(true)
                .message("Refund processed successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Roadmap API: Payment Status by Payment ID
     */
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<ApiResponse<PaymentStatusDto>> getPaymentStatus(@PathVariable Long paymentId) {
        log.info("REST Request: Get Payment Status for paymentId: {}", paymentId);
        PaymentStatusDto statusDto = paymentService.getPaymentStatus(paymentId);
        ApiResponse<PaymentStatusDto> apiResponse = ApiResponse.<PaymentStatusDto>builder()
                .success(true)
                .message("Payment status fetched successfully")
                .data(statusDto)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Roadmap API: Payment Status by Order ID
     */
    @GetMapping("/order/{orderId}/status")
    public ResponseEntity<ApiResponse<PaymentStatusDto>> getPaymentStatusByOrderId(@PathVariable String orderId) {
        log.info("REST Request: Get Payment Status for orderId: {}", orderId);
        PaymentStatusDto statusDto = paymentService.getPaymentStatusByOrderId(orderId);
        ApiResponse<PaymentStatusDto> apiResponse = ApiResponse.<PaymentStatusDto>builder()
                .success(true)
                .message("Payment status fetched successfully")
                .data(statusDto)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Roadmap API: Payment History
     * Retrieves all payment transactions for a given userId.
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getPaymentHistory(
            @RequestParam String userId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("REST Request: Get Payment History for userId: {}", userId);
        
        if (size > 0) {
            Page<PaymentResponseDto> pagedResult = paymentService.getPaymentHistoryPaged(
                    userId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
            ApiResponse<List<PaymentResponseDto>> apiResponse = ApiResponse.<List<PaymentResponseDto>>builder()
                    .success(true)
                    .message("Payment history fetched successfully (Page " + page + ")")
                    .data(pagedResult.getContent())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }

        List<PaymentResponseDto> history = paymentService.getPaymentHistory(userId);
        ApiResponse<List<PaymentResponseDto>> apiResponse = ApiResponse.<List<PaymentResponseDto>>builder()
                .success(true)
                .message("Payment history fetched successfully")
                .data(history)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
