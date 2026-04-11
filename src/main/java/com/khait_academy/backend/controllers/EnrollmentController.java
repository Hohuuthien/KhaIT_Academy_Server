package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.EnrollmentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.services.EnrollmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    /**
     * ENROLL (POST)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @RequestBody EnrollmentRequest request,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        EnrollmentResponse res = enrollmentService.enroll(userId, request.getCourseId());

        return ResponseEntity.ok(
                ApiResponse.<EnrollmentResponse>builder()
                        .success(true)
                        .message("Enroll thành công")
                        .data(res)
                        .build()
        );
    }

    /**
     *  MY COURSES
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> myCourses(
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                ApiResponse.<List<EnrollmentResponse>>builder()
                        .success(true)
                        .message("Danh sách khóa học")
                        .data(enrollmentService.getMyCourses(userId))
                        .build()
        );
    }

    /**
     *  CHECK ENROLLED
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkEnrolled(
            @RequestParam Long courseId,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        boolean enrolled = enrollmentService.isEnrolled(userId, courseId);

        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .success(true)
                        .message("Check enrolled success")
                        .data(enrolled)
                        .build()
        );
    }

    /**
     *  GET USER ID FROM JWT
     */
    private Long getUserId(Authentication authentication) {
        // Nếu bạn có custom UserDetails → cast ra
        // return ((CustomUserDetails) authentication.getPrincipal()).getId();

        // Tạm thời: bạn nên chuyển logic này xuống service sau
        String email = authentication.getName();

        return enrollmentService.getUserIdByEmail(email);
    }
}