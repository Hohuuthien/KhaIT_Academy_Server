package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "quizzes",
    indexes = {
        @Index(name = "idx_quiz_lesson", columnList = "lesson_id"),
        @Index(name = "idx_quiz_published", columnList = "is_published")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== BASIC =====

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonIgnore
    private Lesson lesson;

    // ===== QUIZ CONFIG =====

    @Builder.Default
    @Column(name = "time_limit") // phút
    private Integer timeLimit = 0;

    @Builder.Default
    @Column(name = "max_attempts")
    private Integer maxAttempts = 1;

    @Builder.Default
    @Column(name = "pass_score")
    private Integer passScore = 70; // %

    @Builder.Default
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    // ===== SCHEDULE =====

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    // ===== RELATION QUESTIONS =====

    @OneToMany(
        mappedBy = "quiz",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    // ===== HELPER =====

    public void addQuestion(Question question) {
        questions.add(question);
        question.setQuiz(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setQuiz(null);
    }
}