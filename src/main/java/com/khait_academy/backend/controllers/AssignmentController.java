package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.AssignmentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.services.AssignmentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Validated
public class AssignmentController {

    private final AssignmentService assignmentService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<AssignmentResponse>> create(
            @Valid @RequestBody AssignmentRequest request
    ) {
        AssignmentResponse response = assignmentService.create(request);

        return ResponseEntity
                .created(URI.create("/api/v1/assignments/" + response.getId()))
                .body(success("Create assignment successfully", response));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssignmentResponse>>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(
                success("Get all assignments successfully",
                        assignmentService.getAll(pageable))
        );
    }

    // ================= GET BY LESSON =================
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<Page<AssignmentResponse>>> getByLesson(
            @PathVariable @Min(1) Long lessonId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(
                success("Get assignments by lesson successfully",
                        assignmentService.getByLesson(lessonId, pageable))
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> getById(
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.ok(
                success("Get assignment successfully",
                        assignmentService.getById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody AssignmentRequest request
    ) {
        return ResponseEntity.ok(
                success("Update assignment successfully",
                        assignmentService.update(id, request))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Min(1) Long id
    ) {
        assignmentService.delete(id);
        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .success(true)
                .message("Delete assignment successful")
                .data(null)
                .build()   
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