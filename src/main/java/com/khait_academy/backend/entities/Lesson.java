package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "lessons",
    indexes = {
        @Index(name = "idx_lesson_course", columnList = "course_id"),
        @Index(name = "idx_lesson_order", columnList = "order_index")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lesson extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== BASIC =====

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "slug", unique = true, length = 255)
    private String slug;

    // ===== CONTENT =====

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "duration")
    private Integer duration; // seconds

    // ===== ORDERING =====

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    // ===== FLAGS =====

    @Builder.Default
    @Column(name = "is_preview", nullable = false)
    private Boolean isPreview = false;

    @Builder.Default
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = true;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    // ✅ THAY Question → Quiz
    @OneToMany(
        mappedBy = "lesson",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Quiz> quizzes = new ArrayList<>();

    // ===== HELPER =====

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        quiz.setLesson(this);
    }

    public void removeQuiz(Quiz quiz) {
        quizzes.remove(quiz);
        quiz.setLesson(null);
    }
}