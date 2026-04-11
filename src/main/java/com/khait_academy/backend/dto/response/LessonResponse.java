package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonResponse {

    private Long id;
    private String title;
    private String videoUrl;
    private Integer orderIndex;
    private Long courseId;
}