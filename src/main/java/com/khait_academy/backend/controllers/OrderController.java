// package com.khait_academy.backend.controllers;

// import com.khait_academy.backend.dto.request.OrderRequest;
// import com.khait_academy.backend.dto.response.ApiResponse;
// import com.khait_academy.backend.dto.response.OrderResponse;
// import com.khait_academy.backend.security.UserPrincipal;
// import com.khait_academy.backend.services.OrderService;

// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;

// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/v1/orders")
// @RequiredArgsConstructor
// public class OrderController {

//     private final OrderService orderService;

//     // ================= CREATE ORDER =================

//     @PostMapping
//     public ResponseEntity<ApiResponse<OrderResponse>> create(
//             @Valid @RequestBody OrderRequest request,
//             Authentication authentication
//     ) {

//         Long userId = extractUserId(authentication);

//         OrderResponse response = orderService.createOrder(userId, request);

//         return ResponseEntity.status(HttpStatus.CREATED).body(
//                 ApiResponse.<OrderResponse>builder()
//                         .success(true)
//                         .message("Tạo order thành công")
//                         .data(response)
//                         .build()
//         );
//     }

//     // ================= MY ORDERS (PAGING) =================

//     @GetMapping("/my")
//     public ResponseEntity<ApiResponse<?>> myOrders(
//             Authentication authentication,
//             Pageable pageable
//     ) {

//         Long userId = extractUserId(authentication);

//         var data = orderService.getMyOrders(userId, pageable);

//         return ResponseEntity.ok(
//                 ApiResponse.builder()
//                         .success(true)
//                         .message("Danh sách đơn hàng")
//                         .data(data)
//                         .build()
//         );
//     }

//     // ================= GET ALL (ADMIN) =================

//     @GetMapping("/admin")
//     public ResponseEntity<ApiResponse<?>> getAll(Pageable pageable) {

//         var data = orderService.getAll(pageable);

//         return ResponseEntity.ok(
//                 ApiResponse.builder()
//                         .success(true)
//                         .message("Danh sách tất cả orders")
//                         .data(data)
//                         .build()
//         );
//     }

//     // ================= EXTRACT USER =================

//     private Long extractUserId(Authentication authentication) {

//         if (authentication == null || !authentication.isAuthenticated()) {
//             throw new RuntimeException("Unauthenticated");
//         }

//         Object principal = authentication.getPrincipal();

//         if (principal instanceof UserPrincipal userPrincipal) {
//             return userPrincipal.getId();
//         }

//         throw new RuntimeException("Cannot extract userId from authentication");
//     }
// }