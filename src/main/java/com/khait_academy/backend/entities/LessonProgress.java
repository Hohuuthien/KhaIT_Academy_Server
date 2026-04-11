package com.khait_academy.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "lesson_progress",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_lesson",
            columnNames = {"user_id", "lesson_id"}
        )
    },
    indexes = {
        @Index(name = "idx_user", columnList = "user_id"),
        @Index(name = "idx_lesson", columnList = "lesson_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // lesson
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private Double progress; // 0 → 100

    @Column(name = "last_position")
    private Long lastPosition;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}