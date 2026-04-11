package com.khait_academy.backend.dto.request;

import lombok.Data;

@Data
public class LessonRequest {

    private String title;
    private String videoUrl;
    private Integer orderIndex;
    private Long courseId;
}