package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.QuestionType;

import jakarta.persistence.*;

import lombok.*;

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
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ================= RELATION =================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    // ================= CONTENT =================

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private QuestionType type = QuestionType.MULTIPLE_CHOICE;

    @Builder.Default
    @Column(nullable = false)
    private Integer score = 1;

    // ================= OPTIONS =================

    @Builder.Default
    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<QuestionOption> options = new ArrayList<>();

    // ================= HELPERS =================

    public void addOption(QuestionOption option) {
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(QuestionOption option) {
        options.remove(option);
        option.setQuestion(null);
    }

    public void replaceOptions(List<QuestionOption> newOptions) {
        options.clear();

        if (newOptions != null) {
            newOptions.forEach(this::addOption);
        }
    }

    @PrePersist
    @PreUpdate
    private void validate() {

        if (score == null || score <= 0) {
            throw new IllegalStateException(
                    "Question score must be greater than 0"
            );
        }

        if (content == null || content.isBlank()) {
            throw new IllegalStateException(
                    "Question content is required"
            );
        }
    }
}