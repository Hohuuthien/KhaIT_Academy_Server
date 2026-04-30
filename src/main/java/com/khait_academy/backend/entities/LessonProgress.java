package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "lesson_progress",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_student_lesson",
            columnNames = {"student_id", "lesson_id"}
        )
    },
    indexes = {
        @Index(name = "idx_lp_student", columnList = "student_id"),
        @Index(name = "idx_lp_lesson", columnList = "lesson_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LessonProgress extends BaseEntity {

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
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonIgnore
    private Lesson lesson;

    // ===== PROGRESS =====

    @Builder.Default
    @Column(nullable = false)
    private Boolean completed = false;

    // 🔥 dùng int thay vì Double (0–100)
    @Builder.Default
    @Column(nullable = false)
    private Integer progress = 0;

    // vị trí video (giây)
    @Column(name = "last_position")
    private Integer lastPosition;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // ===== LIFECYCLE =====

    @PrePersist
    public void prePersist() {
        if (progress == null) progress = 0;
        if (completed == null) completed = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastAccessedAt = LocalDateTime.now();

        // auto complete
        if (progress != null && progress == 100 && !completed) {
            this.completed = true;
            this.completedAt = LocalDateTime.now();
        }
    }
}