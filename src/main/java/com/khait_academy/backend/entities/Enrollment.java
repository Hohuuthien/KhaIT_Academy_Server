package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "enrollments",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_enrollment_student_course",
            columnNames = {"student_id", "course_id"}
        )
    },
    indexes = {
        @Index(name = "idx_enrollment_student", columnList = "student_id"),
        @Index(name = "idx_enrollment_course", columnList = "course_id"),
        @Index(name = "idx_enrollment_status", columnList = "status")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Enrollment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====

    // 🔥 FIX QUAN TRỌNG: dùng Student thay vì User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    // ===== BUSINESS =====

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "price_at_purchase", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase;

    // 🔥 PROGRESS nên dùng int (0–100)
    @Builder.Default
    @Column(nullable = false)
    private Integer progress = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    // ===== LIFECYCLE =====

    @PrePersist
    public void prePersist() {
        this.enrolledAt = LocalDateTime.now();

        if (this.progress == null) this.progress = 0;
        if (this.status == null) this.status = EnrollmentStatus.ACTIVE;
    }

    @PreUpdate
    public void preUpdate() {
        // Auto complete nếu đạt 100%
        if (this.progress != null && this.progress == 100 && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
            this.status = EnrollmentStatus.COMPLETED;
        }
    }
}