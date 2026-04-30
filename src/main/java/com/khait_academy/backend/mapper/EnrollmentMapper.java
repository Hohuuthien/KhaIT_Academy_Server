package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.EnrollmentRequest;
import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Enrollment;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.enums.EnrollmentStatus;

public class EnrollmentMapper {

    // ================= CREATE =================
    public static Enrollment toEntity(
            EnrollmentRequest request,
            Student student,
            Course course
    ) {

        EnrollmentStatus status;

        try {
            status = request.getStatus() != null
                    ? EnrollmentStatus.valueOf(request.getStatus().toUpperCase())
                    : EnrollmentStatus.ACTIVE;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid enrollment status");
        }

        return Enrollment.builder()
                .student(student)
                .course(course)
                .priceAtPurchase(request.getPriceAtPurchase())
                .progress(request.getProgress() != null ? request.getProgress() : 0)
                .status(status)
                .expiredAt(request.getExpiredAt())
                .build();
    }

    // ================= RESPONSE =================
    public static EnrollmentResponse toResponse(Enrollment e) {

        return EnrollmentResponse.builder()
                .id(e.getId())

                .studentId(e.getStudent() != null ? e.getStudent().getId() : null)
                .studentName(
                        e.getStudent() != null && e.getStudent().getUser() != null
                                ? e.getStudent().getUser().getFullName()
                                : null
                )

                .courseId(e.getCourse() != null ? e.getCourse().getId() : null)
                .courseTitle(e.getCourse() != null ? e.getCourse().getTitle() : null)

                .enrolledAt(e.getEnrolledAt())
                .priceAtPurchase(e.getPriceAtPurchase())

                .progress(e.getProgress())
                .status(e.getStatus() != null ? e.getStatus().name() : null)

                .completedAt(e.getCompletedAt())
                .expiredAt(e.getExpiredAt())

                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    // ================= UPDATE =================
    public static void updateEntity(Enrollment e, EnrollmentRequest r) {

        if (r.getProgress() != null) e.setProgress(r.getProgress());

        if (r.getStatus() != null) {
            try {
                e.setStatus(EnrollmentStatus.valueOf(r.getStatus().toUpperCase()));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid status");
            }
        }

        if (r.getExpiredAt() != null) e.setExpiredAt(r.getExpiredAt());

        if (r.getPriceAtPurchase() != null) {
            e.setPriceAtPurchase(r.getPriceAtPurchase());
        }
    }
}