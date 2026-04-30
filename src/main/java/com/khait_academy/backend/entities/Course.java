package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "courses",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_course_slug", columnNames = "slug")
    },
    indexes = {
        @Index(name = "idx_course_title", columnList = "title"),
        @Index(name = "idx_course_slug", columnList = "slug"),
        @Index(name = "idx_course_category", columnList = "category_id"),
        @Index(name = "idx_course_teacher", columnList = "teacher_id"),
        @Index(name = "idx_course_status", columnList = "status")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== BASIC =====
    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    // ===== PRICE =====
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "thumbnail_url")
    private String thumbnail;

    // ===== RELATION =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    // 🔥 FIX QUAN TRỌNG: dùng Teacher thay vì User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIgnore
    private Teacher teacher;

    // ===== COURSE INFO =====
    @Column(name = "level", length = 50)
    private String level; // BEGINNER, INTERMEDIATE, ADVANCED

    @Column(name = "duration")
    private Integer duration; // minutes

    // ===== REVIEW =====
    @Builder.Default
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private Integer totalReviews = 0;

    // ===== STATUS =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CourseStatus status = CourseStatus.DRAFT;

    // ===== RELATIONS =====

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts = new ArrayList<>();
}