package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.PostRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.PostResponse;
import com.khait_academy.backend.enums.PostStatus;
import com.khait_academy.backend.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // ===== CREATE =====
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
            @RequestBody @Valid PostRequest request,
            @RequestParam Long authorId // TODO: replace bằng JWT
    ) {
        PostResponse result = postService.create(request, authorId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PostResponse>builder()
                        .success(true)
                        .message("Create post success")
                        .data(result)
                        .build()
        );
    }

    // ===== GET ALL =====
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAll() {
        List<PostResponse> result = postService.getAll();

        return ResponseEntity.ok(
                ApiResponse.<List<PostResponse>>builder()
                        .success(true)
                        .message("Get all posts success")
                        .data(result)
                        .build()
        );
    }

    // ===== GET BY SLUG =====
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PostResponse>> getBySlug(
            @PathVariable String slug
    ) {
        PostResponse result = postService.getBySlug(slug);

        return ResponseEntity.ok(
                ApiResponse.<PostResponse>builder()
                        .success(true)
                        .message("Get post success")
                        .data(result)
                        .build()
        );
    }

    // ===== GET BY STATUS =====
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getByStatus(
            @PathVariable PostStatus status
    ) {
        List<PostResponse> result = postService.getByStatus(status);

        return ResponseEntity.ok(
                ApiResponse.<List<PostResponse>>builder()
                        .success(true)
                        .message("Get posts by status success")
                        .data(result)
                        .build()
        );
    }

    // ===== GET BY CATEGORY =====
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getByCategory(
            @PathVariable Long categoryId
    ) {
        List<PostResponse> result = postService.getByCategory(categoryId);

        return ResponseEntity.ok(
                ApiResponse.<List<PostResponse>>builder()
                        .success(true)
                        .message("Get posts by category success")
                        .data(result)
                        .build()
        );
    }

    // ===== UPDATE =====
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid PostRequest request
    ) {
        PostResponse result = postService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<PostResponse>builder()
                        .success(true)
                        .message("Update post success")
                        .data(result)
                        .build()
        );
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {
        postService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete post success")
                        .build()
        );
    }
}