package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.SubmissionRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.services.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    /**
     * ✅ SUBMIT (Student)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> submit(
            @RequestBody @Valid SubmissionRequest request,
            Authentication authentication
    ) {
        SubmissionResponse response = submissionService.submit(request, authentication);

        return ResponseEntity.ok(
                ApiResponse.<SubmissionResponse>builder()
                        .success(true)
                        .message("Submit assignment success")
                        .data(response)
                        .build()
        );
    }

    /**
     * ✅ GET BY ID
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
     * ✅ GET BY ASSIGNMENT
     */
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getByAssignment(
            @PathVariable Long assignmentId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<SubmissionResponse>>builder()
                        .success(true)
                        .message("Get submissions by assignment success")
                        .data(submissionService.getByAssignment(assignmentId))
                        .build()
        );
    }

    /**
     * ✅ GET BY USER
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getByUser(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<SubmissionResponse>>builder()
                        .success(true)
                        .message("Get submissions by user success")
                        .data(submissionService.getByUser(userId))
                        .build()
        );
    }

    /**
     * ✅ GRADE (Teacher/Admin)
     */
    @PutMapping("/{id}/grade")
    public ResponseEntity<ApiResponse<SubmissionResponse>> grade(
            @PathVariable Long id,
            @RequestParam Double score,
            @RequestParam(required = false) String feedback
    ) {

        return ResponseEntity.ok(
                ApiResponse.<SubmissionResponse>builder()
                        .success(true)
                        .message("Grade submission success")
                        .data(submissionService.grade(id, score, feedback))
                        .build()
        );
    }

    /**
     * ✅ DELETE
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