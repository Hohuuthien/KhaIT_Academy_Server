package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khait_academy.backend.enums.PostStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "posts",
    indexes = {
        @Index(name = "idx_post_slug", columnList = "slug"),
        @Index(name = "idx_post_status", columnList = "status"),
        @Index(name = "idx_post_published", columnList = "published_at"),
        @Index(name = "idx_post_category", columnList = "category_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_slug", columnNames = "slug")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== BASIC =====

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String slug;

    @Column(length = 500)
    private String thumbnail;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    // ===== STATUS =====

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostStatus status;
    // DRAFT, PUBLISHED, ARCHIVED

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    // ===== AUDIT =====

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ===== HELPER =====

    public void publish() {
        this.status = PostStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    public void markDraft() {
        this.status = PostStatus.DRAFT;
        this.publishedAt = null;
    }
}