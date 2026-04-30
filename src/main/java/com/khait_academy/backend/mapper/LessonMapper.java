package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.LessonRequest;
import com.khait_academy.backend.dto.response.LessonResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Lesson;

public class LessonMapper {

    // ================= CREATE =================
    public static Lesson toEntity(LessonRequest request, Course course) {

        return Lesson.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .videoUrl(request.getVideoUrl())
                .content(request.getContent())
                .duration(request.getDuration())
                .orderIndex(request.getOrderIndex())
                .isPreview(request.getIsPreview() != null ? request.getIsPreview() : false)
                .isPublished(request.getIsPublished() != null ? request.getIsPublished() : true)
                .course(course)
                .build();
    }

    // ================= RESPONSE =================
    public static LessonResponse toResponse(Lesson lesson) {

        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .slug(lesson.getSlug())
                .videoUrl(lesson.getVideoUrl())
                .content(lesson.getContent())
                .duration(lesson.getDuration())
                .orderIndex(lesson.getOrderIndex())
                .isPreview(lesson.getIsPreview())
                .isPublished(lesson.getIsPublished())

                .courseId(lesson.getCourse() != null ? lesson.getCourse().getId() : null)
                .courseTitle(lesson.getCourse() != null ? lesson.getCourse().getTitle() : null)

                .totalQuizzes(
                        lesson.getQuizzes() != null ? lesson.getQuizzes().size() : 0
                )
                .build();
    }

    // ================= UPDATE =================
    public static void updateEntity(Lesson lesson, LessonRequest request) {

        if (request.getTitle() != null) lesson.setTitle(request.getTitle());
        if (request.getSlug() != null) lesson.setSlug(request.getSlug());
        if (request.getVideoUrl() != null) lesson.setVideoUrl(request.getVideoUrl());
        if (request.getContent() != null) lesson.setContent(request.getContent());
        if (request.getDuration() != null) lesson.setDuration(request.getDuration());
        if (request.getOrderIndex() != null) lesson.setOrderIndex(request.getOrderIndex());
        if (request.getIsPreview() != null) lesson.setIsPreview(request.getIsPreview());
        if (request.getIsPublished() != null) lesson.setIsPublished(request.getIsPublished());
    }
}