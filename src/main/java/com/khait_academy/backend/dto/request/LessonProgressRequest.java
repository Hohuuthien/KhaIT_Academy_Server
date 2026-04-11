package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonProgressRequest {

    @NotNull(message = "lessonId is required")
    private Long lessonId;

    @NotNull(message = "progress is required")
    @Min(value = 0, message = "progress must be >= 0")
    @Max(value = 100, message = "progress must be <= 100")
    private Double progress; // %

    // vị trí video (giây)
    private Long lastPosition;
}