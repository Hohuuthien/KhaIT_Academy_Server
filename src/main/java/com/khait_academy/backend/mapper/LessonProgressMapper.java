package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.LessonProgressRequest;
import com.khait_academy.backend.dto.response.LessonProgressResponse;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.LessonProgress;
import com.khait_academy.backend.entities.User;

public class LessonProgressMapper {

    // ENTITY -> RESPONSE
    public static LessonProgressResponse toResponse(LessonProgress entity) {
        if (entity == null) return null;

        return LessonProgressResponse.builder()
                .lessonId(entity.getLesson().getId())
                .progress(entity.getProgress())
                .completed(entity.isCompleted())
                .lastPosition(entity.getLastPosition())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // CREATE NEW ENTITY (khi chưa tồn tại)
    public static LessonProgress toEntity(
            LessonProgressRequest request,
            User user,
            Lesson lesson
    ) {
        if (request == null) return null;

        return LessonProgress.builder()
                .user(user)
                .lesson(lesson)
                .progress(clampProgress(request.getProgress()))
                .lastPosition(request.getLastPosition())
                .completed(isCompleted(request.getProgress()))
                .build();
    }

    // UPDATE ENTITY (khi đã tồn tại)
    public static void updateEntity(
            LessonProgress entity,
            LessonProgressRequest request
    ) {
        if (entity == null || request == null) return;

        double progress = clampProgress(request.getProgress());

        entity.setProgress(progress);
        entity.setLastPosition(request.getLastPosition());
        entity.setCompleted(isCompleted(progress));
    }

    // ================= PRIVATE HELPERS =================

    private static double clampProgress(Double progress) {
        if (progress == null) return 0;
        return Math.max(0, Math.min(progress, 100));
    }

    private static boolean isCompleted(Double progress) {
        if (progress == null) return false;
        return progress >= 100;
    }
}