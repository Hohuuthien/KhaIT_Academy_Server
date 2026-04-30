package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LessonProgressRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long lessonId;

    @Min(0)
    @Max(100)
    private Integer progress;

    private Integer lastPosition;
}