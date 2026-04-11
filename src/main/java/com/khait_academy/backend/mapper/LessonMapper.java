package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.LessonResponse;
import com.khait_academy.backend.entities.Lesson;

public class LessonMapper {

    public static LessonResponse toResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .videoUrl(lesson.getVideoUrl())
                .orderIndex(lesson.getOrderIndex())
                .courseId(lesson.getCourse().getId())
                .build();
    }
}