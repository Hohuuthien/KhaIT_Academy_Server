package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Submission;
import com.khait_academy.backend.entities.User;

import org.springframework.data.domain.Page;

import java.util.List;

public class SubmissionMapper {

    private SubmissionMapper() {}

    public static SubmissionResponse toResponse(Submission entity) {

        if (entity == null) return null;

        // 👉 cache object để tránh gọi nhiều lần + tránh lazy issue
        User user = entity.getUser();
        Assignment assignment = entity.getAssignment();

        return SubmissionResponse.builder()
                .id(entity.getId())

                // USER
                .userId(user != null ? user.getId() : null)
                .userName(user != null ? user.getFullName() : null)

                // ASSIGNMENT
                .assignmentId(assignment != null ? assignment.getId() : null)
                .assignmentTitle(assignment != null ? assignment.getTitle() : null)

                // SUBMISSION
                .fileUrl(entity.getFileUrl())
                .score(entity.getScore())
                .feedback(entity.getFeedback())
                .status(entity.getStatus())
                .submittedAt(entity.getSubmittedAt())

                // META
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }

    // ✅ map list
    public static List<SubmissionResponse> toList(List<Submission> list) {
        if (list == null || list.isEmpty()) return List.of();
        return list.stream().map(SubmissionMapper::toResponse).toList();
    }

    // ✅ map page (QUAN TRỌNG khi dùng pagination)
    public static Page<SubmissionResponse> toPage(Page<Submission> page) {
        return page.map(SubmissionMapper::toResponse);
    }
}