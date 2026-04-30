package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.LessonProgressResponse;
import com.khait_academy.backend.entities.LessonProgress;

public class LessonProgressMapper {

    public static LessonProgressResponse toResponse(LessonProgress lp) {

        return LessonProgressResponse.builder()
                .id(lp.getId())

                .studentId(lp.getStudent() != null ? lp.getStudent().getId() : null)
                .studentName(lp.getStudent() != null && lp.getStudent().getUser() != null
                        ? lp.getStudent().getUser().getFullName()
                        : null)

                .lessonId(lp.getLesson() != null ? lp.getLesson().getId() : null)
                .lessonTitle(lp.getLesson() != null ? lp.getLesson().getTitle() : null)

                .progress(lp.getProgress())
                .completed(lp.getCompleted())

                .lastPosition(lp.getLastPosition())
                .lastAccessedAt(lp.getLastAccessedAt())
                .completedAt(lp.getCompletedAt())

                .build();
    }
}