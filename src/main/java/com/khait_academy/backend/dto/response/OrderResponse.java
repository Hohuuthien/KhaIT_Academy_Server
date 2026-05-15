package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.OrderStatus;
import com.khait_academy.backend.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private PaymentMethod paymentMethod;

    private String transactionId;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}