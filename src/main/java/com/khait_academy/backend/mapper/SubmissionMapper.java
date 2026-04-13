package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.entities.Submission;

public class SubmissionMapper {

    public static SubmissionResponse toResponse(Submission entity) {

        if (entity == null) return null;

        return SubmissionResponse.builder()
                .id(entity.getId())

                // user
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getFullName() : null)

                // assignment
                .assignmentId(entity.getAssignment() != null ? entity.getAssignment().getId() : null)
                .assignmentTitle(entity.getAssignment() != null ? entity.getAssignment().getTitle() : null)

                // submission
                .fileUrl(entity.getFileUrl())
                .score(entity.getScore())
                .feedback(entity.getFeedback())
                .status(entity.getStatus())
                .submittedAt(entity.getSubmittedAt())

                // nếu bạn có thêm
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }
}