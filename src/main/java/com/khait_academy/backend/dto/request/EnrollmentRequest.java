package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollmentRequest {

    @NotNull(message = "CourseId không được để trống")
    private Long courseId;
}