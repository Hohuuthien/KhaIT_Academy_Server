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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    // ================= SUBMIT =================
    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> submit(
            @Valid @RequestBody SubmissionRequest request,
            Authentication authentication
    ) {

        SubmissionResponse response =
                submissionService.submit(request, authentication);

        return ResponseEntity.status(201)
                .body(ApiResponse.success("Submit assignment success", response));
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getById(@PathVariable Long id) {

        SubmissionResponse response =
                submissionService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Get submission success", response)
        );
    }

    // ================= BY ASSIGNMENT =================
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<ApiResponse<Page<SubmissionResponse>>> getByAssignment(
            @PathVariable Long assignmentId,
            Pageable pageable
    ) {

        Page<SubmissionResponse> response =
                submissionService.getByAssignment(assignmentId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Get submissions by assignment success", response)
        );
    }

    // ================= MY SUBMISSIONS =================
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<SubmissionResponse>>> getMySubmissions(
            Authentication authentication,
            Pageable pageable
    ) {

        Page<SubmissionResponse> response =
                submissionService.getMySubmissions(authentication, pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Get my submissions success", response)
        );
    }

    // ================= GRADE =================
    @PutMapping("/{id}/grade")
    public ResponseEntity<ApiResponse<SubmissionResponse>> grade(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequest request
    ) {

        SubmissionResponse response =
                submissionService.grade(
                        id,
                        request.getScore(),
                        request.getFeedback()
                );

        return ResponseEntity.ok(
                ApiResponse.success("Grade submission success", response)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        submissionService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Delete submission success", null)
        );
    }
}