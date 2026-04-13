package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.EnrollmentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.services.EnrollmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // ===== ENROLL =====
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @RequestBody @Valid EnrollmentRequest request,
            Authentication authentication
    ) {

        String email = extractEmail(authentication);

        EnrollmentResponse res = enrollmentService.enrollByEmail(email, request.getCourseId());

        log.info("User {} enrolled course {}", email, request.getCourseId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<EnrollmentResponse>builder()
                        .success(true)
                        .message("Đăng ký khóa học thành công")
                        .data(res)
                        .build()
        );
    }

    // ===== MY COURSES =====
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> myCourses(
            Authentication authentication
    ) {

        String email = extractEmail(authentication);

        List<EnrollmentResponse> data = enrollmentService.getMyCoursesByEmail(email);

        return ResponseEntity.ok(
                ApiResponse.<List<EnrollmentResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách khóa học thành công")
                        .data(data)
                        .build()
        );
    }

    // ===== CHECK ENROLLED =====
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkEnrolled(
            @RequestParam Long courseId,
            Authentication authentication
    ) {

        if (courseId == null) {
            throw new BadRequestException("courseId không được null");
        }

        String email = extractEmail(authentication);

        boolean enrolled = enrollmentService.isEnrolledByEmail(email, courseId);

        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .success(true)
                        .message("Kiểm tra trạng thái đăng ký thành công")
                        .data(enrolled)
                        .build()
        );
    }

    // ===== HELPER =====
    private String extractEmail(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Unauthorized access attempt");
            throw new BadRequestException("Bạn chưa đăng nhập");
        }

        return authentication.getName(); // email từ JWT
    }
}