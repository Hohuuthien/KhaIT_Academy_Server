package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.AttendanceRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.services.AttendanceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * CHECK-IN / UPDATE
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(
            @Valid @RequestBody AttendanceRequest request
    ) {

        AttendanceResponse response = attendanceService.checkIn(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<AttendanceResponse>builder()
                                .success(true)
                                .message("Check-in success")
                                .data(response)
                                .build()
                );
    }

    /**
     * GET BY LESSON (PAGINATION) 🔥
     */
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getByLesson(
            @PathVariable Long lessonId,
            Pageable pageable
    ) {

        Page<AttendanceResponse> page = attendanceService.getByLesson(lessonId, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<AttendanceResponse>>builder()
                        .success(true)
                        .message("Get attendance by lesson success")
                        .data(page)
                        .build()
        );
    }

    /**
     * GET BY USER (PAGINATION) 🔥
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getByUser(
            @PathVariable Long userId,
            Pageable pageable
    ) {

        Page<AttendanceResponse> page = attendanceService.getByUser(userId, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<AttendanceResponse>>builder()
                        .success(true)
                        .message("Get attendance by user success")
                        .data(page)
                        .build()
        );
    }

    /**
     * DELETE
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {

        attendanceService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        ApiResponse.<Void>builder()
                                .success(true)
                                .message("Delete attendance success")
                                .build()
                );
    }
}