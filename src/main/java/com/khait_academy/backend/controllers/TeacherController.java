package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.TeacherRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.TeacherResponse;
import com.khait_academy.backend.services.TeacherService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Validated
public class TeacherController {

    private final TeacherService teacherService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> create(
            @Valid @RequestBody TeacherRequest request) {

        TeacherResponse response = teacherService.create(request);

        return ResponseEntity
                .created(URI.create("/api/teachers/" + response.getId()))
                .body(success("Create teacher successfully", response));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getAll() {

        return ResponseEntity.ok(
                success("Get teachers successfully", teacherService.getAll())
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getById(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                success("Get teacher successfully", teacherService.getById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody TeacherRequest request) {

        return ResponseEntity.ok(
                success("Update teacher successfully",
                        teacherService.update(id, request))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Min(1) Long id) {

        teacherService.delete(id);
        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .success(true)
                .message("Delete teacher successful")
                .data(null)
                .build()
            
        );
    }

    // ================= CHANGE STATUS =================
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TeacherResponse>> changeStatus(
            @PathVariable @Min(1) Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                success("Change teacher status successfully",
                        teacherService.changeStatus(id, status))
        );
    }

    // ================= GET BY USER =================
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getByUserId(
            @PathVariable @Min(1) Long userId) {

        return ResponseEntity.ok(
                success("Get teacher by user successfully",
                        teacherService.getByUserId(userId))
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