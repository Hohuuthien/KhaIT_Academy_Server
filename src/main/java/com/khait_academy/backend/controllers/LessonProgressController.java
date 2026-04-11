package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.LessonProgressRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.LessonProgressResponse;
import com.khait_academy.backend.services.LessonProgressService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class LessonProgressController {

    private final LessonProgressService lessonProgressService;

    /**
     *  UPDATE PROGRESS
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LessonProgressResponse>> updateProgress(
            @RequestBody LessonProgressRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<LessonProgressResponse>builder()
                        .success(true)
                        .message("Cập nhật tiến độ thành công")
                        .data(lessonProgressService.updateProgress(email, request))
                        .build()
        );
    }

    /**
     *  GET PROGRESS BY COURSE
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<LessonProgressResponse>>> getByCourse(
            @PathVariable Long courseId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<List<LessonProgressResponse>>builder()
                        .success(true)
                        .message("Danh sách tiến độ theo course")
                        .data(lessonProgressService.getProgressByCourse(email, courseId))
                        .build()
        );
    }

    /**
     *  GET PROGRESS BY LESSON
     */
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<LessonProgressResponse>> getByLesson(
            @PathVariable Long lessonId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<LessonProgressResponse>builder()
                        .success(true)
                        .message("Tiến độ lesson")
                        .data(lessonProgressService.getProgressByLesson(email, lessonId))
                        .build()
        );
    }
}