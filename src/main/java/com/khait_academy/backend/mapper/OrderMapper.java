package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.OrderItemResponse;
import com.khait_academy.backend.dto.response.OrderResponse;
import com.khait_academy.backend.entities.Order;
import com.khait_academy.backend.entities.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .transactionId(order.getTransactionId())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }

    public OrderItemResponse toItemResponse(OrderItem item) {

        return OrderItemResponse.builder()
                .id(item.getId())
                .courseId(item.getCourse().getId())
                .courseTitle(item.getCourseTitle())
                .courseThumbnail(item.getCourseThumbnail())
                .price(item.getPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}