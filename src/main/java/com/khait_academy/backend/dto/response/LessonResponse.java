package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonResponse {

    private Long id;

    // BASIC
    private String title;
    private String slug;

    // CONTENT
    private String videoUrl;
    private String content;
    private Integer duration;

    // ORDER
    private Integer orderIndex;

    // FLAGS
    private Boolean isPreview;
    private Boolean isPublished;

    // RELATION
    private Long courseId;
    private String courseTitle;

    // STATS (optional expand later)
    private Integer totalQuizzes;
}