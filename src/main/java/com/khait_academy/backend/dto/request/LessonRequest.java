package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LessonRequest {

    // ===== BASIC =====
    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String slug;

    // ===== CONTENT =====
    @Size(max = 500)
    private String videoUrl;

    private String content;

    private Integer duration; // seconds

    // ===== ORDER =====
    @NotNull(message = "Order index is required")
    private Integer orderIndex;

    // ===== FLAGS =====
    private Boolean isPreview;

    private Boolean isPublished = true;

    // ===== RELATION =====
    @NotNull(message = "CourseId is required")
    private Long courseId;
}