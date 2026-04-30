package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.CourseRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.CourseResponse;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.services.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // ✅ FIX
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // ================= GET ALL / SEARCH / FILTER =================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double rating,
            @PageableDefault(
                    size = 10,
                    sort = "id", // 🔥 SAFE hơn createdAt
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {

        // ===== VALIDATION =====
        if (minPrice != null && minPrice < 0) {
            throw new BadRequestException("minPrice must be >= 0");
        }

        if (maxPrice != null && maxPrice < 0) {
            throw new BadRequestException("maxPrice must be >= 0");
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new BadRequestException("minPrice must be <= maxPrice");
        }

        if (rating != null && (rating < 0 || rating > 5)) {
            throw new BadRequestException("rating must be between 0 and 5");
        }

        Page<CourseResponse> result = courseService.getCourses(
                keyword, categoryId, minPrice, maxPrice, rating, pageable
        );

        String message = result.isEmpty()
                ? "No courses found"
                : "Get courses successfully";

        return ok(message, result);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getById(@PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid course id");
        }

        CourseResponse response = courseService.getById(id);

        return ok("Get course successfully", response);
    }

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> create(
            @Valid @RequestBody CourseRequest request
    ) {

        CourseResponse response = courseService.create(request);

        return ResponseEntity
                .created(URI.create("/api/v1/courses/" + response.getId()))
                .body(
                        ApiResponse.<CourseResponse>builder()
                                .success(true)
                                .message("Create course successfully")
                                .data(response)
                                .build()
                );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request
    ) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid course id");
        }

        CourseResponse response = courseService.update(id, request);

        return ok("Update course successfully", response);
    }

    // ================= DELETE =================
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        if (id == null || id <= 0) {
                throw new BadRequestException("Invalid course id");
        }

        courseService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete course successfully")
                        .data(null)
                        .build()
        );
        }

    // ================= COMMON RESPONSE =================
    private <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .success(true)
                        .message(message)
                        .data(data)
                        .build()
        );
    }
}