package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Post;
import com.khait_academy.backend.enums.PostStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ===== DETAIL =====
    @EntityGraph(attributePaths = {"author", "category"})
    Optional<Post> findBySlug(String slug);

    @EntityGraph(attributePaths = {"author", "category"})
    Optional<Post> findById(Long id);

    // ===== LIST =====
    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findByStatusOrderByCreatedAtDesc(PostStatus status);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findByCategoryIdOrderByCreatedAtDesc(Long categoryId);

    // ===== VALIDATION =====
    boolean existsBySlug(String slug);
}