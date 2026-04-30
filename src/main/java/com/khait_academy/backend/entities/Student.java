package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.StudentStatus;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "students",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_user", columnNames = "user_id")
    },
    indexes = {
        @Index(name = "idx_student_parent", columnList = "parent_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== USER (AUTH) =====
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // ===== PARENT RELATION =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Parent parent;

    // ===== PROFILE =====
    @Column(name = "grade_level", length = 20)
    private String gradeLevel;

    @Column(name = "school_name", length = 255)
    private String schoolName;

    // ===== LEARNING =====
    @Column(name = "learning_goal", columnDefinition = "TEXT")
    private String learningGoal;

    @Column(name = "average_score", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal averageScore = BigDecimal.ZERO;

    // ===== STATUS =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;
}