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
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // ================= CHECK-IN =================
    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(
            @Valid @RequestBody AttendanceRequest request
    ) {

        AttendanceResponse response = attendanceService.checkIn(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Check-in success", response));
    }

    // ================= BY LESSON =================
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getByLesson(
            @PathVariable Long lessonId,
            Pageable pageable
    ) {

        Page<AttendanceResponse> response =
                attendanceService.getByLesson(lessonId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Get attendance by lesson success", response)
        );
    }

    // ================= BY STUDENT =================
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getByStudent(
            @PathVariable Long studentId,
            Pageable pageable
    ) {

        Page<AttendanceResponse> response =
                attendanceService.getByStudent(studentId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Get attendance by student success", response)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        attendanceService.delete(id);

        return ResponseEntity.ok(
            ApiResponse.success("Delete attendance successful", null)
        );
                
    }
}