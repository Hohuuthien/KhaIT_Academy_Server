package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.*;
import com.khait_academy.backend.entities.*;

import java.util.List;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .transactionId(order.getTransactionId())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(OrderMapper::toItemResponse)
                        .toList())
                .build();
    }

    public static OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .courseId(item.getCourse().getId())
                .courseTitle(item.getCourse().getTitle())
                .price(item.getPrice())
                .build();
    }
}