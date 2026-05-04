package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.StudentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.StudentResponse;
import com.khait_academy.backend.services.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.create(request);

        return ResponseEntity
                .created(URI.create("/api/students/" + response.getId()))
                .body(ApiResponse.success("Create student successfully", response));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {

        List<StudentResponse> data = studentService.getAll();

        return ResponseEntity.ok(
                data.isEmpty()
                        ? ApiResponse.success("No students found", data)
                        : ApiResponse.success("Get students successfully", data)
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(
            @PathVariable Long id) {

        StudentResponse response = studentService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Get student successfully", response)
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("Update student successfully", response)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {

        studentService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Delete student successfully", null)
        );
    }

    // ================= GET BY USER =================
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<StudentResponse>> getByUserId(
            @PathVariable Long userId) {

        StudentResponse response = studentService.getByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.success("Get student by user successfully", response)
        );
    }
}