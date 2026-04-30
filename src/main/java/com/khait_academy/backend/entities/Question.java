package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.QuestionType;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "questions",
    indexes = {
        @Index(name = "idx_question_quiz", columnList = "quiz_id"),
        @Index(name = "idx_question_type", columnList = "type")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Question extends BaseEntity { // ✅ FIX 1

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz; // ❌ bỏ JsonIgnore nếu dùng DTO

    // ===== CONTENT =====
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private QuestionType type;

    @Column(nullable = false)
    private Integer score;

    // ===== OPTIONS =====
    @OneToMany(
        mappedBy = "question",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<QuestionOption> options = new ArrayList<>();

    // ===== HELPER =====
    public void addOption(QuestionOption option) {
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(QuestionOption option) {
        options.remove(option);
        option.setQuestion(null);
    }
}