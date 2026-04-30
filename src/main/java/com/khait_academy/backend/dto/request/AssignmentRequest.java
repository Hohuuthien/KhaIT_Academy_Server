package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AssignmentRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull
    private Long lessonId;

    private LocalDateTime dueDate;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal maxScore;

    private Boolean isPublished;

    private Boolean allowLateSubmission;

    private Integer maxAttempts;
}