package com.khait_academy.backend.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuizRequest {

    @NotNull
    private Long lessonId;

    @NotBlank
    private String title;

    private String description;

    private Integer timeLimit;
    private Integer maxAttempts;
    private Integer passScore;

    private Boolean isPublished;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
