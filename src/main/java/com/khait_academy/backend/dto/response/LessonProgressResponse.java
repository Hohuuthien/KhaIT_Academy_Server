package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LessonProgressResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long lessonId;
    private String lessonTitle;

    private Boolean completed;
    private Integer progress;

    private Integer lastPosition;

    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
}