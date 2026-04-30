package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AssignmentResponse {

    private Long id;

    private String title;
    private String description;

    // lesson
    private Long lessonId;
    private String lessonTitle;

    // business
    private LocalDateTime dueDate;
    private BigDecimal maxScore;
    private Boolean isPublished;

    private Boolean allowLateSubmission;
    private Integer maxAttempts;

    // audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}