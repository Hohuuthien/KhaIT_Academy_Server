package com.khait_academy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TeacherResponse {

    private Long id;

    // ===== USER INFO =====
    private Long userId;
    private String username;

    // ===== PROFILE =====
    private String fullName;
    private String email;
    private String bio;
    private Integer experienceYears;
    private String specialization;

    // ===== RATING =====
    private BigDecimal averageRating;
    private Integer totalReviews;

    // ===== STATUS =====
    private String status;

    // ===== AUDIT =====
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}