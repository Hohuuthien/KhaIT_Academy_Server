package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.AssignmentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.services.AssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    /**
     * CREATE
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AssignmentResponse>> create(
            @RequestBody @Valid AssignmentRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<AssignmentResponse>builder()
                                .success(true)
                                .message("Create assignment success")
                                .data(assignmentService.create(request))
                                .build()
                );
    }

    /**
     *  GET ALL (pagination)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssignmentResponse>>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.<Page<AssignmentResponse>>builder()
                        .success(true)
                        .message("Get all assignments success")
                        .data(assignmentService.getAll(pageable))
                        .build()
        );
    }

    /**
     *  GET BY LESSON (pagination)
     */
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<Page<AssignmentResponse>>> getByLesson(
            @PathVariable Long lessonId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.<Page<AssignmentResponse>>builder()
                        .success(true)
                        .message("Get assignments by lesson success")
                        .data(assignmentService.getByLesson(lessonId, pageable))
                        .build()
        );
    }

    /**
     *  GET BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<AssignmentResponse>builder()
                        .success(true)
                        .message("Get assignment success")
                        .data(assignmentService.getById(id))
                        .build()
        );
    }

    /**
     *  UPDATE
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid AssignmentRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<AssignmentResponse>builder()
                        .success(true)
                        .message("Update assignment success")
                        .data(assignmentService.update(id, request))
                        .build()
        );
    }

    /**
     *  DELETE
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {

        assignmentService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        ApiResponse.<Void>builder()
                                .success(true)
                                .message("Delete assignment success")
                                .build()
                );
    }
}