package com.khait_academy.backend.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizResponse {

    private Long id;

    private Long lessonId;

    private String title;
    private String description;

    private Integer timeLimit;
    private Integer maxAttempts;
    private Integer passScore;

    private Boolean isPublished;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
