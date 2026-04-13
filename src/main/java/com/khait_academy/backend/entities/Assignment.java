    package com.khait_academy.backend.entities;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "assignments")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Assignment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String title;

    
        @Lob
        @Column(columnDefinition = "TEXT")
        private String description;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "lesson_id", nullable = false)
        private Lesson lesson;

        private LocalDateTime dueDate;
        private Double maxScore;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }