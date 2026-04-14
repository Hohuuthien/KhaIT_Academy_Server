package com.khait_academy.backend.entities;

import com.khait_academy.backend.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "attendances",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_lesson", columnNames = {"user_id", "lesson_id"})
    },
    indexes = {
        @Index(name = "idx_attendance_user", columnList = "user_id"),
        @Index(name = "idx_attendance_lesson", columnList = "lesson_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LAZY để tránh load thừa
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    @Column(nullable = false)
    private LocalDateTime attendedAt;

    @Column(length = 500)
    private String note;

    // Tự set timestamp khi insert
    @PrePersist
    public void prePersist() {
        if (this.attendedAt == null) {
            this.attendedAt = LocalDateTime.now();
        }
    }
}