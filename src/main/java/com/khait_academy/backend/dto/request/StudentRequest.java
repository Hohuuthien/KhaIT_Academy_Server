package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentRequest {

    @NotNull(message = "UserId is required")
    private Long userId;

    private Long parentId;

    @Size(max = 20)
    private String gradeLevel;

    @Size(max = 255)
    private String schoolName;

    private String learningGoal;

    private Double averageScore;

    private String status;
}