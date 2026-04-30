package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    // ===== RATING =====
    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải từ 1 đến 5")
    @Max(value = 5, message = "Rating phải từ 1 đến 5")
    private Integer rating;

    // ===== COMMENT =====
    @Size(max = 1000, message = "Comment tối đa 1000 ký tự")
    private String comment;

    // ===== COURSE =====
    @NotNull(message = "CourseId không được để trống")
    private Long courseId;
}