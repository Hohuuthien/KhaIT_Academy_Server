package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.TeacherStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
    name = "teachers",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_teacher_user", columnNames = "user_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== RELATION WITH USER =====
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;
    // ===== PROFILE =====
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "specialization", length = 255)
    private String specialization;

    // ===== RATING =====
    @Column(name = "average_rating", precision = 2, scale = 1)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;

    // ===== STATUS =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TeacherStatus status = TeacherStatus.ACTIVE;
}