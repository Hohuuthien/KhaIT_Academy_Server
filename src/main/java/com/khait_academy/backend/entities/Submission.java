package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "submissions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_student_assignment",
            columnNames = {"student_id", "assignment_id"}
        )
    },
    indexes = {
        @Index(name = "idx_submission_student", columnList = "student_id"),
        @Index(name = "idx_submission_assignment", columnList = "assignment_id"),
        @Index(name = "idx_submission_status", columnList = "status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    @JsonIgnore
    private Assignment assignment;

    // ===== SUBMISSION DATA =====

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    // ===== LIFECYCLE =====

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = SubmissionStatus.SUBMITTED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // auto grading logic
        if (this.score != null && this.gradedAt == null) {
            this.gradedAt = LocalDateTime.now();
            this.status = SubmissionStatus.GRADED;
        }
    }
}