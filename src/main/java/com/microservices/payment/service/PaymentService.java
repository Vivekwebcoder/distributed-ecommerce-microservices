package com.microservices.payment.service;

import com.microservices.payment.dto.*;
import com.microservices.payment.dto.event.PaymentInitiatedEvent;
import com.microservices.payment.dto.event.RefundInitiatedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto processPayment(PaymentRequestDto requestDto);

    void processPaymentFromEvent(PaymentInitiatedEvent event);

    RefundResponseDto processRefund(RefundRequestDto requestDto);

    void processRefundFromEvent(RefundInitiatedEvent event);

    PaymentStatusDto getPaymentStatus(Long paymentId);

    PaymentStatusDto getPaymentStatusByOrderId(String orderId);

    List<PaymentResponseDto> getPaymentHistory(String userId);

    Page<PaymentResponseDto> getPaymentHistoryPaged(String userId, Pageable pageable);
}
