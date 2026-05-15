package com.khait_academy.backend.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.khait_academy.backend.dto.request.OrderRequest;
import com.khait_academy.backend.dto.response.OrderResponse;
import com.khait_academy.backend.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestParam Long userId,
            @Valid @RequestBody OrderRequest request
    ) {

        return ResponseEntity.ok(
                orderService.createOrder(userId, request)
        );
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @RequestParam Long userId
    ) {

        return ResponseEntity.ok(
                orderService.getMyOrders(userId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long id
    ) {

        orderService.cancelOrder(id);

        return ResponseEntity.ok("Order cancelled successfully");
    }

    @PutMapping("/{id}/paid")
    public ResponseEntity<String> markPaid(
            @PathVariable Long id,
            @RequestParam String transactionId
    ) {

        orderService.markPaid(id, transactionId);

        return ResponseEntity.ok("Order paid successfully");
    }
}