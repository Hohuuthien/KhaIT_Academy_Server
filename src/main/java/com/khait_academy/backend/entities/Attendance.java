package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "attendances",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_student_lesson_attendance",
            columnNames = {"student_id", "lesson_id"}
        )
    },
    indexes = {
        @Index(name = "idx_attendance_student", columnList = "student_id"),
        @Index(name = "idx_attendance_lesson", columnList = "lesson_id"),
        @Index(name = "idx_attendance_status", columnList = "status"),
        @Index(name = "idx_attendance_date", columnList = "attended_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====

    // 🔥 FIX: dùng Student thay vì User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonIgnore
    private Lesson lesson;

    // ===== ATTENDANCE =====

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;
    // PRESENT | ABSENT | LATE

    @Column(name = "attended_at", nullable = false)
    private LocalDateTime attendedAt;

    @Column(length = 500)
    private String note;

    // ===== OPTIONAL (PRO LEVEL) =====

    @Column(name = "checked_by")
    private Long checkedBy; // teacherId hoặc staffId

    // ===== LIFECYCLE =====

    @PrePersist
    public void prePersist() {
        if (attendedAt == null) {
            attendedAt = LocalDateTime.now();
        }
    }
}