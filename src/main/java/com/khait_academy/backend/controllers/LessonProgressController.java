package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.LessonProgressRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.LessonProgressResponse;
import com.khait_academy.backend.security.UserPrincipal;
import com.khait_academy.backend.services.LessonProgressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson-progress")
@RequiredArgsConstructor
public class LessonProgressController {

    private final LessonProgressService lessonProgressService;

    /**
     * STUDENT
     * CREATE / UPDATE MY PROGRESS
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LessonProgressResponse>> saveOrUpdate(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody LessonProgressRequest request
    ) {

        LessonProgressResponse response =
                lessonProgressService.saveOrUpdate(
                        user.getId(),
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.<LessonProgressResponse>builder()
                        .success(true)
                        .message("Save your learning progress")
                        .data(response)
                        .build()
        );
    }

    /**
     * STUDENT
     * GET MY PROGRESS
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<LessonProgressResponse>>> getMyProgress(
            @AuthenticationPrincipal UserPrincipal user
    ) {

        List<LessonProgressResponse> progressList =
                lessonProgressService.getMyProgress(user.getId());

        return ResponseEntity.ok(
                ApiResponse.<List<LessonProgressResponse>>builder()
                        .success(true)
                        .message("Learning progress list")
                        .data(progressList)
                        .build()
        );
    }

    /**
     * ADMIN / TEACHER
     * GET PROGRESS BY LESSON
     */
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<List<LessonProgressResponse>>> getByLesson(
            @PathVariable Long lessonId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<LessonProgressResponse>>builder()
                        .success(true)
                        .message("Lesson progress list")
                        .data(lessonProgressService.getByLesson(lessonId))
                        .build()
        );
    }

    /**
     * ADMIN ONLY
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable Long id
    ) {

        lessonProgressService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Delete successful progress")
                        .data("OK")
                        .build()
        );
    }
}