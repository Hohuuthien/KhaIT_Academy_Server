package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "user_answers",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_attempt_question",
            columnNames = {"attempt_id", "question_id"}
        )
    },
    indexes = {
        @Index(name = "idx_answer_attempt", columnList = "attempt_id"),
        @Index(name = "idx_answer_question", columnList = "question_id")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attempt_id", nullable = false)
    @JsonIgnore
    private QuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // ===== ANSWER =====

    // dùng cho text / fill / fallback
    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    // dùng cho trắc nghiệm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private QuestionOption selectedOption;

    // ===== RESULT =====

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "score")
    private Double score;
}