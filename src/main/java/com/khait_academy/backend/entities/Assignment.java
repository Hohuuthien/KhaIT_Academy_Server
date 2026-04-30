package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "assignments",
    indexes = {
        @Index(name = "idx_assignment_lesson", columnList = "lesson_id"),
        @Index(name = "idx_assignment_due_date", columnList = "due_date")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Assignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== BASIC =====

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonIgnore
    private Lesson lesson;

    // ===== BUSINESS =====

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    // 🔥 dùng BigDecimal thay vì Double
    @Column(name = "max_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal maxScore;

    @Builder.Default
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    // ===== OPTIONAL (PRO LEVEL) =====

    @Column(name = "allow_late_submission", nullable = false)
    @Builder.Default
    private Boolean allowLateSubmission = false;

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    // ===== LIFECYCLE =====

    @PrePersist
    public void prePersist() {
        if (maxScore == null) {
            maxScore = BigDecimal.valueOf(10); // default 10 điểm
        }
        if (isPublished == null) isPublished = false;
        if (allowLateSubmission == null) allowLateSubmission = false;
    }
}