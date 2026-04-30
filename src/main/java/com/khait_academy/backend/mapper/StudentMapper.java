package com.khait_academy.backend.mapper;

import java.math.BigDecimal;

import com.khait_academy.backend.dto.request.StudentRequest;
import com.khait_academy.backend.dto.response.StudentResponse;
import com.khait_academy.backend.entities.*;

import com.khait_academy.backend.enums.StudentStatus;

public class StudentMapper {

    // ================= CREATE =================
    public static Student toEntity(StudentRequest request, User user, Parent parent) {

        return Student.builder()
                .user(user)
                .parent(parent)
                .gradeLevel(request.getGradeLevel())
                .schoolName(request.getSchoolName())
                .learningGoal(request.getLearningGoal())
                .averageScore(
                        request.getAverageScore() != null
                                ? BigDecimal.valueOf(request.getAverageScore())
                                : BigDecimal.ZERO
                )
                .status(
                        request.getStatus() != null
                                ? StudentStatus.valueOf(request.getStatus().toUpperCase())
                                : StudentStatus.ACTIVE
                )
                .build();
    }

    // ================= RESPONSE =================
    public static StudentResponse toResponse(Student student) {

        if (student == null) return null;

        User user = student.getUser();
        Parent parent = student.getParent();

        return StudentResponse.builder()
                .id(student.getId())

                // USER
                .userId(user != null ? user.getId() : null)
                .studentName(user != null ? user.getFullName() : null)
                .email(user != null ? user.getEmail() : null)

                // PARENT
                .parentId(parent != null ? parent.getId() : null)
                .parentName(parent != null && parent.getUser() != null
                        ? parent.getUser().getFullName()
                        : null)

                // PROFILE
                .gradeLevel(student.getGradeLevel())
                .schoolName(student.getSchoolName())
                .learningGoal(student.getLearningGoal())
                .averageScore(
                        student.getAverageScore() != null
                                ? student.getAverageScore().doubleValue()
                                : null
                )

                // STATUS
                .status(student.getStatus() != null
                        ? student.getStatus().name()
                        : null)

                // AUDIT
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())

                .build();
    }

    // ================= UPDATE =================
    public static void updateEntity(Student student, StudentRequest request, Parent parent) {

        if (request.getGradeLevel() != null) {
            student.setGradeLevel(request.getGradeLevel());
        }

        if (request.getSchoolName() != null) {
            student.setSchoolName(request.getSchoolName());
        }

        if (request.getLearningGoal() != null) {
            student.setLearningGoal(request.getLearningGoal());
        }

        if (request.getAverageScore() != null) {
            student.setAverageScore(BigDecimal.valueOf(request.getAverageScore()));
        }

        if (request.getStatus() != null) {
            student.setStatus(
                    StudentStatus.valueOf(request.getStatus().toUpperCase())
            );
        }

        if (parent != null) {
            student.setParent(parent);
        }
    }
}