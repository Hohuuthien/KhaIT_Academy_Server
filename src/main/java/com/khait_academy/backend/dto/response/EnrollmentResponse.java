package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EnrollmentResponse {

    private Long id;

    private Long courseId;
    private String courseTitle;
    private String courseThumbnail;
    private Double coursePrice;

    private LocalDateTime enrolledAt;

    private boolean completed;
}