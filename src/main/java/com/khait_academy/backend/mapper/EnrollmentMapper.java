package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Enrollment;

public class EnrollmentMapper {

    public static EnrollmentResponse toResponse(Enrollment enrollment) {

        if (enrollment == null) {
            return null;
        }

        Course course = enrollment.getCourse();

        return EnrollmentResponse.builder()
                .id(enrollment.getId())

                // ===== COURSE =====
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .courseThumbnail(course != null ? course.getThumbnail() : null)
                .coursePrice(course != null ? course.getPrice() : null)

                // ===== ENROLLMENT =====
                .enrolledAt(enrollment.getEnrolledAt())
                .completed(enrollment.isCompleted())

                .build();
    }
}