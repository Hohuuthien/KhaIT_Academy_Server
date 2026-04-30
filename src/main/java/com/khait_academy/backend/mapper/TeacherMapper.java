package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.TeacherRequest;
import com.khait_academy.backend.dto.response.TeacherResponse;
import com.khait_academy.backend.entities.Teacher;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.TeacherStatus;

import java.math.BigDecimal;

public class TeacherMapper {

    // ================= CREATE ENTITY =================
    public static Teacher toEntity(TeacherRequest request, User user) {

        return Teacher.builder()
                .user(user)
                .fullName(request.getFullName() != null
                        ? request.getFullName()
                        : user.getFullName())
                .email(request.getEmail() != null
                        ? request.getEmail()
                        : user.getEmail())
                .bio(request.getBio())
                .experienceYears(request.getExperienceYears())
                .specialization(request.getSpecialization())
                .status(
                        request.getStatus() != null
                                ? TeacherStatus.valueOf(request.getStatus().toUpperCase())
                                : TeacherStatus.ACTIVE
                )
                .averageRating(BigDecimal.ZERO)
                .totalReviews(0)
                .build();
    }

    // ================= RESPONSE =================
    public static TeacherResponse toResponse(Teacher teacher) {

        if (teacher == null) return null;

        User user = teacher.getUser();

        return TeacherResponse.builder()
                .id(teacher.getId())

                // USER INFO
                .userId(user != null ? user.getId() : null)
                .username(user != null ? user.getFullName() : null)

                // PROFILE
                .fullName(user != null ? user.getFullName() : teacher.getFullName())
                .email(user != null ? user.getEmail() : teacher.getEmail())
                .bio(teacher.getBio())
                .experienceYears(teacher.getExperienceYears())
                .specialization(teacher.getSpecialization())

                // RATING
                .averageRating(teacher.getAverageRating())
                .totalReviews(teacher.getTotalReviews())

                // STATUS
                .status(
                        teacher.getStatus() != null
                                ? teacher.getStatus().name()
                                : null
                )

                // AUDIT
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())

                .build();
    }

    // ================= UPDATE ENTITY =================
    public static void updateEntity(Teacher teacher, TeacherRequest request, User user) {

        if (request.getFullName() != null) {
            teacher.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            teacher.setEmail(request.getEmail());
        }

        if (request.getBio() != null) {
            teacher.setBio(request.getBio());
        }

        if (request.getExperienceYears() != null) {
            teacher.setExperienceYears(request.getExperienceYears());
        }

        if (request.getSpecialization() != null) {
            teacher.setSpecialization(request.getSpecialization());
        }

        if (request.getStatus() != null) {
            teacher.setStatus(
                    TeacherStatus.valueOf(request.getStatus().toUpperCase())
            );
        }

        // sync user nếu cần override
        if (user != null) {
            teacher.setUser(user);
        }
    }
}