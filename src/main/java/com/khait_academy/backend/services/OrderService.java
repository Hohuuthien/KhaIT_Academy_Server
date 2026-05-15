package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.OrderRequest;
import com.khait_academy.backend.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(Long userId, OrderRequest request);

    List<OrderResponse> getMyOrders(Long userId);

    OrderResponse getOrderById(Long id);

    void cancelOrder(Long id);

    void markPaid(Long id, String transactionId);
}