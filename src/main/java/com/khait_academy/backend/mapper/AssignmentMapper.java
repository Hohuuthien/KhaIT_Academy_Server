package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.AssignmentRequest;
import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Lesson;

public class AssignmentMapper {

    // ===== CREATE =====
    public static Assignment toEntity(AssignmentRequest request, Lesson lesson) {

        return Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .lesson(lesson)
                .dueDate(request.getDueDate())
                .maxScore(request.getMaxScore())
                .isPublished(request.getIsPublished() != null ? request.getIsPublished() : false)
                .allowLateSubmission(
                        request.getAllowLateSubmission() != null ? request.getAllowLateSubmission() : false
                )
                .maxAttempts(request.getMaxAttempts())
                .build();
    }

    // ===== RESPONSE =====
    public static AssignmentResponse toResponse(Assignment a) {

        return AssignmentResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .description(a.getDescription())

                .lessonId(a.getLesson() != null ? a.getLesson().getId() : null)
                .lessonTitle(a.getLesson() != null ? a.getLesson().getTitle() : null)

                .dueDate(a.getDueDate())
                .maxScore(a.getMaxScore())
                .isPublished(a.getIsPublished())

                .allowLateSubmission(a.getAllowLateSubmission())
                .maxAttempts(a.getMaxAttempts())

                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }

    // ===== UPDATE =====
    public static void updateEntity(Assignment a, AssignmentRequest r) {

        if (r.getTitle() != null) a.setTitle(r.getTitle());
        if (r.getDescription() != null) a.setDescription(r.getDescription());
        if (r.getDueDate() != null) a.setDueDate(r.getDueDate());
        if (r.getMaxScore() != null) a.setMaxScore(r.getMaxScore());
        if (r.getIsPublished() != null) a.setIsPublished(r.getIsPublished());
        if (r.getAllowLateSubmission() != null) a.setAllowLateSubmission(r.getAllowLateSubmission());
        if (r.getMaxAttempts() != null) a.setMaxAttempts(r.getMaxAttempts());
    }
}