package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.CategoryRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.CategoryResponse;
import com.khait_academy.backend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * CREATE CATEGORY
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @RequestBody @Valid CategoryRequest request
    ) {
        return ResponseEntity.status(201).body(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Create category success")
                        .data(categoryService.create(request))
                        .build()
        );
    }

    /**
     * GET ALL ROOT TREE
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .success(true)
                        .message("Get categories tree success")
                        .data(categoryService.getAll())
                        .build()
        );
    }

    /**
     * GET BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Get category success")
                        .data(categoryService.getById(id))
                        .build()
        );
    }

    /**
     * UPDATE
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Update category success")
                        .data(categoryService.update(id, request))
                        .build()
        );
    }

    /**
     * DELETE (REST STANDARD = 204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET CHILDREN
     */
    @GetMapping("/{id}/children")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getChildren(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .success(true)
                        .message("Get children success")
                        .data(categoryService.getChildren(id))
                        .build()
        );
    }
}