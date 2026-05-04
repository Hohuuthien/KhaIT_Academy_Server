package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.PostRequest;
import com.khait_academy.backend.dto.response.PostResponse;
import com.khait_academy.backend.entities.Category;
import com.khait_academy.backend.entities.Post;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.PostStatus;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
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

    // ================= CREATE =================

    @Transactional
    public PostResponse create(PostRequest request, Long userId) {

        validateSlug(request.getSlug());

        User author = getUser(userId);
        Category category = getCategory(request.getCategoryId());

        Post post = Post.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .thumbnail(request.getThumbnail())
                .content(request.getContent())
                .author(author)
                .category(category)
                .status(request.getStatus())
                .build();

        applyPublishLogic(post, request.getStatus());

        return PostMapper.toResponse(
                postRepository.save(post)
        );
    }

    // ================= GET =================

    public List<PostResponse> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    public PostResponse getBySlug(String slug) {
        return PostMapper.toResponse(getPost(slug));
    }

    public List<PostResponse> getByCategory(Long categoryId) {
        return postRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId)
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    public List<PostResponse> getByStatus(PostStatus status) {
        return postRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    // ================= UPDATE =================

    @Transactional
    public PostResponse update(Long id, PostRequest request) {

        Post post = getPost(id);

        validateSlugForUpdate(post, request.getSlug());

        Category category = getCategory(request.getCategoryId());

        post.setTitle(request.getTitle());
        post.setSlug(request.getSlug());
        post.setThumbnail(request.getThumbnail());
        post.setContent(request.getContent());
        post.setCategory(category);

        updateStatus(post, request.getStatus());

        post.setUpdatedAt(LocalDateTime.now());

        return PostMapper.toResponse(
                postRepository.save(post)
        );
    }

    // ================= DELETE =================

    @Transactional
    public void delete(Long id) {

        Post post = getPost(id);

        postRepository.delete(post);
    }

    // ================= PRIVATE HELPERS =================

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));
    }

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));
    }

    private Post getPost(String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));
    }

    private void validateSlug(String slug) {
        if (postRepository.existsBySlug(slug)) {
            throw new BadRequestException("Slug already exists");
        }
    }

    private void validateSlugForUpdate(Post post, String slug) {
        if (!post.getSlug().equals(slug)
                && postRepository.existsBySlug(slug)) {
            throw new BadRequestException("Slug already exists");
        }
    }

    private void applyPublishLogic(Post post, PostStatus status) {
        if (status == PostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }
    }

    private void updateStatus(Post post, PostStatus newStatus) {

        if (post.getStatus() != PostStatus.PUBLISHED
                && newStatus == PostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }

        post.setStatus(newStatus);
    }
}