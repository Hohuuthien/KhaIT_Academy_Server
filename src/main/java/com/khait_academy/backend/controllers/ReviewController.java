package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.ReviewRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.ReviewResponse;
import com.khait_academy.backend.services.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ================= CREATE REVIEW =================

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> create(
            @Valid @RequestBody ReviewRequest request,
            @RequestParam Long studentId // 👉 sau này thay bằng JWT
    ) {

        ReviewResponse response =
                reviewService.create(request, studentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ReviewResponse>builder()
                        .success(true)
                        .message("Review created successfully")
                        .data(response)
                        .build()
        );
    }

    // ================= GET BY COURSE =================

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) Long currentStudentId
    ) {

        List<ReviewResponse> responses =
                reviewService.getByCourse(courseId, currentStudentId);

        return ResponseEntity.ok(
                ApiResponse.<List<ReviewResponse>>builder()
                        .success(true)
                        .message("Reviews retrieved successfully")
                        .data(responses)
                        .build()
        );
    }

    // ================= APPROVE REVIEW (ADMIN) =================

    @PatchMapping("/{reviewId}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(
            @PathVariable Long reviewId
    ) {

        reviewService.approve(reviewId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Review approved successfully")
                        .build()
        );
    }

    // ================= DELETE REVIEW =================

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long reviewId,
            @RequestParam Long studentId
    ) {

        reviewService.delete(reviewId, studentId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Review deleted successfully")
                        .build()
        );
    }
}