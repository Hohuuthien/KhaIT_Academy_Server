package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {

    // ===== BASIC =====
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Pattern(
        regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
        message = "Slug must be lowercase and kebab-case"
    )
    private String slug;

    @Size(max = 5000)
    private String description;

    // ===== PRICE =====
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @Size(max = 500)
    private String thumbnail;

    // ===== RELATION =====
    @NotNull
    private Long categoryId;

    @NotNull
    private Long teacherId;

    // ===== COURSE OPTIONS =====
    private String level;

    private Integer duration;

    // ===== STATUS (optional admin only) =====
    private String status;
}