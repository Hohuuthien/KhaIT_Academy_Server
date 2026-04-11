package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LessonProgressResponse {

    private Long lessonId;

    private Double progress; // %

    private boolean completed;

    private Long lastPosition; // seconds

    private LocalDateTime updatedAt;
}