package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(
    name = "reviews",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_student_course_review",
            columnNames = {"student_id", "course_id"}
        )
    },
    indexes = {
        @Index(name = "idx_review_course", columnList = "course_id"),
        @Index(name = "idx_review_student", columnList = "student_id"),
        @Index(name = "idx_review_approved", columnList = "is_approved")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====

    // 🔥 FIX QUAN TRỌNG: dùng Student thay vì User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    // ===== REVIEW =====

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Builder.Default
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;

    // ===== OPTIONAL (PRO LEVEL) =====

    @Column(name = "is_edited", nullable = false)
    @Builder.Default
    private Boolean isEdited = false;

    @Column(name = "edited_at")
    private java.time.LocalDateTime editedAt;

    // ===== LIFECYCLE =====

    @PrePersist
    public void prePersist() {
        if (isApproved == null) isApproved = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.isEdited = true;
        this.editedAt = java.time.LocalDateTime.now();
    }
}