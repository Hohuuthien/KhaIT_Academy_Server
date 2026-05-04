package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.PostRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.PostResponse;
import com.khait_academy.backend.enums.PostStatus;
import com.khait_academy.backend.security.UserPrincipal;
import com.khait_academy.backend.services.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * CREATE POST
     * Author lấy từ JWT
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {

        PostResponse result = postService.create(
                request,
                user.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PostResponse>builder()
                        .success(true)
                        .message("Create post success")
                        .data(result)
                        .build()
        );
    }

    /**
     * GET ALL POSTS
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<PostResponse>>builder()
                        .success(true)
                        .message("Get all posts success")
                        .data(postService.getAll())
                        .build()
        );
    }

    /**
     * GET POST BY SLUG
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PostResponse>> getBySlug(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                ApiResponse.<PostResponse>builder()
                        .success(true)
                        .message("Get post success")
                        .data(postService.getBySlug(slug))
                        .build()
        );
    }

    /**
     * GET POSTS BY STATUS
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getByStatus(
            @PathVariable PostStatus status
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<PostResponse>>builder()
                        .success(true)
                        .message("Get posts by status success")
                        .data(postService.getByStatus(status))
                        .build()
        );
    }

    /**
     * GET POSTS BY CATEGORY
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getByCategory(
            @PathVariable Long categoryId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<PostResponse>>builder()
                        .success(true)
                        .message("Get posts by category success")
                        .data(postService.getByCategory(categoryId))
                        .build()
        );
    }

    /**
     * UPDATE POST
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<PostResponse>builder()
                        .success(true)
                        .message("Update post success")
                        .data(postService.update(id, request))
                        .build()
        );
    }

    /**
     * DELETE POST
     */
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