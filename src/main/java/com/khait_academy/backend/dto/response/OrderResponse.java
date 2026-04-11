package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long id;
    private Double totalPrice;
    private OrderStatus status;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}