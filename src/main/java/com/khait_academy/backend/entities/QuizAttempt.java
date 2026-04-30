package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khait_academy.backend.enums.AttemptStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "quiz_attempts",
    indexes = {
        @Index(name = "idx_attempt_user", columnList = "user_id"),
        @Index(name = "idx_attempt_quiz", columnList = "quiz_id")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnore
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // ===== ATTEMPT INFO =====

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttemptStatus status;
    // IN_PROGRESS, SUBMITTED, GRADED

    // ===== SCORE =====

    @Column
    private Double score;

    @Column(name = "total_score")
    private Double totalScore;

    // ===== TIME =====

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    // ===== RELATION ANSWERS =====

    @OneToMany(
        mappedBy = "attempt",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<UserAnswer> answers = new ArrayList<>();

    // ===== HELPER =====

    public void addAnswer(UserAnswer answer) {
        answers.add(answer);
        answer.setAttempt(this);
    }

    public void submit() {
        this.status = AttemptStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    // ===== LIFECYCLE =====

    @PrePersist
    public void prePersist() {
        if (this.startedAt == null) {
            this.startedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = AttemptStatus.IN_PROGRESS;
        }
    }
}