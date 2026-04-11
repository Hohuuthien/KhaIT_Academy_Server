package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.OrderRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.OrderResponse;
import com.khait_academy.backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // tạo order
    @PostMapping
    public ApiResponse<OrderResponse> create(
            @RequestBody OrderRequest request,
            Authentication auth
    ) {
        return ApiResponse.<OrderResponse>builder()
                .success(true)
                .message("Tạo order thành công")
                .data(orderService.createOrder(auth.getName(), request))
                .build();
    }

    // lấy order của user
    @GetMapping("/my")
    public ApiResponse<List<OrderResponse>> myOrders(Authentication auth) {

        return ApiResponse.<List<OrderResponse>>builder()
                .success(true)
                .message("Danh sách đơn hàng")
                .data(orderService.getMyOrders(auth.getName()))
                .build();
    }
}