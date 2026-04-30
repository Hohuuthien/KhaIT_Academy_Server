// package com.khait_academy.backend.controllers;

// import com.khait_academy.backend.dto.request.ReviewRequest;
// import com.khait_academy.backend.dto.response.*;
// import com.khait_academy.backend.security.SecurityUtils;
// import com.khait_academy.backend.services.ReviewService;

// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// import org.springframework.http.*;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/v1/reviews")
// @RequiredArgsConstructor
// @Slf4j
// public class ReviewController {

//     private final ReviewService reviewService;
//     private final SecurityUtils securityUtils;

//     // ================= CREATE =================

//     @PostMapping
//     public ResponseEntity<ApiResponse<ReviewResponse>> create(
//             @Valid @RequestBody ReviewRequest request,
//             Authentication authentication
//     ) {

//         Long userId = securityUtils.getCurrentUserId(authentication);

//         ReviewResponse res = reviewService.create(request, userId);

//         log.info("Create review: userId={}, courseId={}", userId, request.getCourseId());

//         return ResponseEntity.status(HttpStatus.CREATED).body(
//                 ApiResponse.<ReviewResponse>builder()
//                         .success(true)
//                         .message("Review created, waiting for approval")
//                         .data(res)
//                         .build()
//         );
//     }

//     // ================= GET BY COURSE =================

//     @GetMapping("/course/{courseId}")
//     public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByCourse(
//             @PathVariable Long courseId,
//             Authentication authentication
//     ) {

//         Long userId = securityUtils.getCurrentUserId(authentication);

//         List<ReviewResponse> data =
//                 reviewService.getByCourse(courseId, userId);

//         return ResponseEntity.ok(
//                 ApiResponse.<List<ReviewResponse>>builder()
//                         .success(true)
//                         .message("Get reviews success")
//                         .data(data)
//                         .build()
//         );
//     }

//     // ================= APPROVE (ADMIN) =================

//     @PreAuthorize("hasRole('ADMIN')")
//     @PutMapping("/{id}/approve")
//     public ResponseEntity<ApiResponse<Void>> approve(@PathVariable Long id) {

//         reviewService.approve(id);

//         log.info("Approve review: reviewId={}", id);

//         return ResponseEntity.ok(
//                 ApiResponse.<Void>builder()
//                         .success(true)
//                         .message("Review approved")
//                         .build()
//         );
//     }

//     // ================= DELETE =================

//     @DeleteMapping("/{id}")
//     public ResponseEntity<ApiResponse<Void>> delete(
//             @PathVariable Long id,
//             Authentication authentication
//     ) {

//         Long userId = securityUtils.getCurrentUserId(authentication);

//         reviewService.delete(id, userId);

//         log.info("Delete review: userId={}, reviewId={}", userId, id);

//         return ResponseEntity.ok(
//                 ApiResponse.<Void>builder()
//                         .success(true)
//                         .message("Delete review success")
//                         .build()
//         );
//     }
// }