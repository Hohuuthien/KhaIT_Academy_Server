package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.EnrollmentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.services.EnrollmentService;

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
                .created(URI.create("/api/v1/enrollments/" + response.getId()))
                .body(
                        ApiResponse.<EnrollmentResponse>builder()
                                .success(true)
                                .message("Enroll course successfully")
                                .data(response)
                                .build()
                );
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getAll() {

        List<EnrollmentResponse> responses = enrollmentService.getAll();

        return ok(
                responses.isEmpty()
                        ? "No enrollments found"
                        : "Get enrollments successfully",
                responses
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> getById(
            @PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid enrollment id");
        }

        EnrollmentResponse response = enrollmentService.getById(id);

        return ok("Get enrollment successfully", response);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentRequest request) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid enrollment id");
        }

        EnrollmentResponse response = enrollmentService.update(id, request);

        return ok("Update enrollment successfully", response);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid enrollment id");
        }

        enrollmentService.delete(id);

        return ok("Delete enrollment successfully", null);
    }

    // ================= BY STUDENT =================
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByStudent(
            @PathVariable Long studentId) {

        if (studentId == null || studentId <= 0) {
            throw new BadRequestException("Invalid student id");
        }

        List<EnrollmentResponse> responses = enrollmentService.getByStudent(studentId);

        return ok(
                responses.isEmpty()
                        ? "No enrollments found for this student"
                        : "Get enrollments by student successfully",
                responses
        );
    }

    // ================= BY COURSE =================
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByCourse(
            @PathVariable Long courseId) {

        if (courseId == null || courseId <= 0) {
            throw new BadRequestException("Invalid course id");
        }

        List<EnrollmentResponse> responses = enrollmentService.getByCourse(courseId);

        return ok(
                responses.isEmpty()
                        ? "No enrollments found for this course"
                        : "Get enrollments by course successfully",
                responses
        );
    }

    // ================= COMMON RESPONSE =================
    private <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .success(true)
                        .message(message)
                        .data(data)
                        .build()
        );
    }
}