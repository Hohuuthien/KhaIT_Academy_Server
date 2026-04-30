package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.StudentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.StudentResponse;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.services.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.create(request);

        return ResponseEntity
                .created(URI.create("/api/v1/students/" + response.getId()))
                .body(
                        ApiResponse.<StudentResponse>builder()
                                .success(true)
                                .message("Create student successfully")
                                .data(response)
                                .build()
                );
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {

        List<StudentResponse> responses = studentService.getAll();

        return ok(
                responses.isEmpty()
                        ? "No students found"
                        : "Get students successfully",
                responses
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(
            @PathVariable Long id) {

        validateId(id, "student");

        StudentResponse response = studentService.getById(id);

        return ok("Get student successfully", response);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        validateId(id, "student");

        StudentResponse response = studentService.update(id, request);

        return ok("Update student successfully", response);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {

        validateId(id, "student");

        studentService.delete(id);

        return ok("Delete student successfully", null);
    }

    // ================= GET BY USER ID =================
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<StudentResponse>> getByUserId(
            @PathVariable Long userId) {

        validateId(userId, "user");

        StudentResponse response = studentService.getByUserId(userId);

        return ok("Get student by user successfully", response);
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

    // ================= VALIDATE ID =================
    private void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid " + fieldName + " id");
        }
    }
}