package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.entities.Assignment;

public class AssignmentMapper {

    public static AssignmentResponse toResponse(Assignment entity) {
        return AssignmentResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .lessonId(entity.getLesson().getId())
                .lessonTitle(entity.getLesson().getTitle())
                .dueDate(entity.getDueDate())
                .maxScore(entity.getMaxScore())
                .build();
    }
}