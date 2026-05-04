package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.EnrollmentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.services.EnrollmentService;
import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> create(
            @Valid @RequestBody EnrollmentRequest request) {

        EnrollmentResponse response = enrollmentService.create(request);

        return ResponseEntity
                .created(URI.create("/api/enrollments/" + response.getId()))
                .body(success("Enroll course successfully", response));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getAll() {
        return ResponseEntity.ok(
                success("Get enrollments successfully", enrollmentService.getAll())
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> getById(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                success("Get enrollment successfully", enrollmentService.getById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody EnrollmentRequest request) {

        return ResponseEntity.ok(
                success("Update enrollment successfully",
                        enrollmentService.update(id, request))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(1) Long id) {

        enrollmentService.delete(id);
        return ResponseEntity.noContent().build(); // ✅ chuẩn REST
    }

    // ================= BY STUDENT =================
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByStudent(
            @PathVariable @Min(1) Long studentId) {

        return ResponseEntity.ok(
                success("Get enrollments by student successfully",
                        enrollmentService.getByStudent(studentId))
        );
    }

    // ================= BY COURSE =================
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByCourse(
            @PathVariable @Min(1) Long courseId) {

        return ResponseEntity.ok(
                success("Get enrollments by course successfully",
                        enrollmentService.getByCourse(courseId))
        );
    }

    // ================= COMMON RESPONSE =================
    private <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}