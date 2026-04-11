package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.entities.Enrollment;

public class EnrollmentMapper {

   public static EnrollmentResponse toResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .courseThumbnail(enrollment.getCourse().getThumbnail())
                .coursePrice(enrollment.getCourse().getPrice())
                .enrolledAt(enrollment.getEnrolledAt())
                .completed(enrollment.isCompleted())
                .build();
    }
}