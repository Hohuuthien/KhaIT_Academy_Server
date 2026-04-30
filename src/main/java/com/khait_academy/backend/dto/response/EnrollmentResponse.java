package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class EnrollmentResponse {

    private Long id;

    // student
    private Long studentId;
    private String studentName;

    // course
    private Long courseId;
    private String courseTitle;

    // business
    private LocalDateTime enrolledAt;
    private BigDecimal priceAtPurchase;

    private Integer progress;
    private String status;

    private LocalDateTime completedAt;
    private LocalDateTime expiredAt;

    // audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}