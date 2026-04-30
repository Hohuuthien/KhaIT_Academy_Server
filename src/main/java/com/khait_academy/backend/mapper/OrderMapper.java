package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.OrderItemResponse;
import com.khait_academy.backend.dto.response.OrderResponse;
import com.khait_academy.backend.entities.Order;
import com.khait_academy.backend.entities.OrderItem;
import com.khait_academy.backend.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    /**
     * ENTITY → RESPONSE
     */
    public static OrderResponse toResponse(Order order) {

        if (order == null) {
            return null;
        }

        User user = order.getUser();

        return OrderResponse.builder()
                .id(order.getId())

                // ===== USER =====
                .userId(user != null ? user.getId() : null)
                .userName(user != null ? user.getFullName() : null)

                // ===== PRICE =====
                .totalPrice(order.getTotalPrice())

                // ===== STATUS =====
                .status(order.getStatus())

                // ===== PAYMENT =====
                .paymentMethod(order.getPaymentMethod())
                .transactionId(order.getTransactionId())

                // ===== TIME =====
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())

                // ===== ITEMS =====
                .items(
                        order.getItems() != null
                                ? order.getItems()
                                        .stream()
                                        .map(OrderMapper::toItemResponse)
                                        .collect(Collectors.toList())
                                : Collections.emptyList()
                )

                .build();
    }

    /**
     * ITEM → RESPONSE
     */
    public static OrderItemResponse toItemResponse(OrderItem item) {

        if (item == null) {
            return null;
        }

        return OrderItemResponse.builder()
                .courseId(
                        item.getCourse() != null
                                ? item.getCourse().getId()
                                : null
                )
                .courseTitle(
                        item.getCourse() != null
                                ? item.getCourse().getTitle()
                                : null
                )
                .price(item.getPrice())
                .build();
    }
}