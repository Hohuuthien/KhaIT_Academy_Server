package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.GradeRequest;
import com.khait_academy.backend.dto.request.SubmissionRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.services.SubmissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    /**
     *  SUBMIT (Student)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> submit(
            @Valid @RequestBody SubmissionRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SubmissionResponse>builder()
                        .success(true)
                        .message("Submit assignment success")
                        .data(submissionService.submit(request, authentication))
                        .build());
    }

    /**
     * GET BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<SubmissionResponse>builder()
                        .success(true)
                        .message("Get submission success")
                        .data(submissionService.getById(id))
                        .build()
        );
    }

    /**
     * GET BY ASSIGNMENT (PAGINATION)
     */
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<ApiResponse<Page<SubmissionResponse>>> getByAssignment(
            @PathVariable Long assignmentId,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.<Page<SubmissionResponse>>builder()
                        .success(true)
                        .message("Get submissions by assignment success")
                        .data(submissionService.getByAssignment(assignmentId, pageable))
                        .build()
        );
    }

    /*
     *  GET MY SUBMISSIONS (SECURE)
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<SubmissionResponse>>> getMySubmissions(
            Authentication authentication,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.<Page<SubmissionResponse>>builder()
                        .success(true)
                        .message("Get my submissions success")
                        .data(submissionService.getMySubmissions(authentication, pageable))
                        .build()
        );
    }

    /**
     *  GRADE (Teacher/Admin)
     */
    @PutMapping("/{id}/grade")
    public ResponseEntity<ApiResponse<SubmissionResponse>> grade(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<SubmissionResponse>builder()
                        .success(true)
                        .message("Grade submission success")
                        .data(submissionService.grade(id, request.getScore(), request.getFeedback()))
                        .build()
        );
    }

    /**
     *  DELETE
     */
    @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        submissionService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete submission success")
                        .build()
        );
        }
}