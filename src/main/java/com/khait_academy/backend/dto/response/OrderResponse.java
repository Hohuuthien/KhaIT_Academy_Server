package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.OrderStatus;
import com.khait_academy.backend.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {

    private Long id;

    // ===== USER =====
    private Long userId;
    private String userName;

    // ===== PRICE =====
    private BigDecimal totalPrice;

    // ===== STATUS =====
    private OrderStatus status;

    // ===== PAYMENT =====
    private PaymentMethod paymentMethod;
    private String transactionId;

    // ===== TIME =====
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== ITEMS =====
    private List<OrderItemResponse> items;
}