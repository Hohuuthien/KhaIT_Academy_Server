package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.PostRequest;
import com.khait_academy.backend.dto.response.PostResponse;
import com.khait_academy.backend.entities.Category;
import com.khait_academy.backend.entities.Post;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.PostStatus;
import com.khait_academy.backend.mapper.PostMapper;
import com.khait_academy.backend.repositories.CategoryRepository;
import com.khait_academy.backend.repositories.PostRepository;
import com.khait_academy.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // ===== CREATE =====
    @Transactional
    public PostResponse create(PostRequest request, Long authorId) {

        if (postRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Post post = Post.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .thumbnail(request.getThumbnail())
                .content(request.getContent())
                .author(author)
                .category(category)
                .status(request.getStatus())
                .build();

        // publish logic
        if (request.getStatus() == PostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }

        post.setUpdatedAt(null);

        return PostMapper.toResponse(postRepository.save(post));
    }

    // ===== GET ALL =====
    public List<PostResponse> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    // ===== GET BY SLUG =====
    public PostResponse getBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .map(PostMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // ===== GET BY CATEGORY =====
    public List<PostResponse> getByCategory(Long categoryId) {
        return postRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId)
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    // ===== GET BY STATUS =====
    public List<PostResponse> getByStatus(PostStatus status) {
        return postRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    // ===== UPDATE =====
    @Transactional
    public PostResponse update(Long id, PostRequest request) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // check slug duplicate
        if (!post.getSlug().equals(request.getSlug())
                && postRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        post.setTitle(request.getTitle());
        post.setSlug(request.getSlug());
        post.setThumbnail(request.getThumbnail());
        post.setContent(request.getContent());
        post.setStatus(request.getStatus());

        // publish logic
        if (request.getStatus() == PostStatus.PUBLISHED && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }

        post.setUpdatedAt(LocalDateTime.now());

        return PostMapper.toResponse(postRepository.save(post));
    }

    // ===== DELETE =====
    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found");
        }
        postRepository.deleteById(id);
    }
}