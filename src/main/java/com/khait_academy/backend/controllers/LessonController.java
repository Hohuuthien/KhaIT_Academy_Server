package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.LessonRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.LessonResponse;
import com.khait_academy.backend.services.LessonService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    /**
     *  CREATE (ADMIN)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LessonResponse>> create(@RequestBody LessonRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<LessonResponse>builder()
                        .success(true)
                        .message("Tạo lesson thành công")
                        .data(lessonService.create(request))
                        .build()
        );
    }

    /**
     *  GET LESSON BY COURSE
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getByCourse(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                ApiResponse.<List<LessonResponse>>builder()
                        .success(true)
                        .message("Danh sách lesson")
                        .data(lessonService.getByCourse(courseId))
                        .build()
        );
    }

    /**
     *  UPDATE
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonResponse>> update(
            @PathVariable Long id,
            @RequestBody LessonRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<LessonResponse>builder()
                        .success(true)
                        .message("Cập nhật thành công")
                        .data(lessonService.update(id, request))
                        .build()
        );
    }

    /**
     *  DELETE
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {

        lessonService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Xóa thành công")
                        .data("OK")
                        .build()
        );
    }
}