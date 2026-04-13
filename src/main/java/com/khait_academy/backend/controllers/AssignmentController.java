package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.AssignmentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.services.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    /**
     *  CREATE ASSIGNMENT
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AssignmentResponse>> create(
            @RequestBody @Valid AssignmentRequest request
    ) {

        AssignmentResponse assignment = assignmentService.create(request);

        return ResponseEntity.ok(
                ApiResponse.<AssignmentResponse>builder()
                        .success(true)
                        .message("Create assignment success")
                        .data(assignment)
                        .build()
        );
    }

    /**
     *  GET ASSIGNMENTS BY LESSON
     */
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> getByLesson(
            @PathVariable Long lessonId
    ) {

        List<AssignmentResponse> assignments = assignmentService.getByLesson(lessonId);

        return ResponseEntity.ok(
                ApiResponse.<List<AssignmentResponse>>builder()
                        .success(true)
                        .message("Get assignments by lesson success")
                        .data(assignments)
                        .build()
        );
    }

    /**
     *  GET ALL (OPTIONAL - nếu bạn thêm service)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> getAll() {

        List<AssignmentResponse> assignments = assignmentService.getAll();

        return ResponseEntity.ok(
                ApiResponse.<List<AssignmentResponse>>builder()
                        .success(true)
                        .message("Get all assignments success")
                        .data(assignments)
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

        AssignmentResponse assignment = assignmentService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.<AssignmentResponse>builder()
                        .success(true)
                        .message("Get assignment success")
                        .data(assignment)
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

        AssignmentResponse assignment = assignmentService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<AssignmentResponse>builder()
                        .success(true)
                        .message("Update assignment success")
                        .data(assignment)
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

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete assignment success")
                        .build()
        );
    }
}