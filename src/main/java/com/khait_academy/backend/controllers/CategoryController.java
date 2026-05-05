package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.CategoryRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.CategoryResponse;
import com.khait_academy.backend.services.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryService.create(request);

        return ResponseEntity
                .created(URI.create("/api/categories/" + response.getId()))
                .body(success("Create category successfully", response));
    }

    // ================= GET ALL ROOT =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(
                success("Get categories tree successfully",
                        categoryService.getAll())
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.ok(
                success("Get category successfully",
                        categoryService.getById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(
                success("Update category successfully",
                        categoryService.update(id, request))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Min(1) Long id
    ) {
        categoryService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete category successfull")
                        .data(null)
                        .build()
        );
    }

    // ================= GET CHILDREN =================
    @GetMapping("/{id}/children")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getChildren(
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.ok(
                success("Get children successfully",
                        categoryService.getChildren(id))
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