package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.entities.Submission;
import com.khait_academy.backend.entities.User;

import org.springframework.data.domain.Page;

import java.util.List;

public class SubmissionMapper {

    private SubmissionMapper() {}

    public static SubmissionResponse toResponse(Submission entity) {

        if (entity == null) return null;

        // ===== SAFE NAVIGATION =====
        Student student = entity.getStudent();
        User user = (student != null) ? student.getUser() : null;
        Assignment assignment = entity.getAssignment();

        return SubmissionResponse.builder()
                .id(entity.getId())

                // ===== STUDENT =====
                .studentId(student != null ? student.getId() : null)
                .studentName(user != null ? user.getFullName() : null)

                // ===== ASSIGNMENT =====
                .assignmentId(assignment != null ? assignment.getId() : null)
                .assignmentTitle(assignment != null ? assignment.getTitle() : null)

                // ===== SUBMISSION =====
                .fileUrl(entity.getFileUrl())
                .score(entity.getScore())
                .feedback(entity.getFeedback())
                .status(entity.getStatus())

                // ===== TIME =====
                .submittedAt(entity.getSubmittedAt())
                .gradedAt(entity.getGradedAt())

                // ===== META =====
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }

    // ===== LIST =====
    public static List<SubmissionResponse> toList(List<Submission> list) {
        if (list == null || list.isEmpty()) return List.of();
        return list.stream().map(SubmissionMapper::toResponse).toList();
    }

    // ===== PAGE =====
    public static Page<SubmissionResponse> toPage(Page<Submission> page) {
        return page.map(SubmissionMapper::toResponse);
    }
}