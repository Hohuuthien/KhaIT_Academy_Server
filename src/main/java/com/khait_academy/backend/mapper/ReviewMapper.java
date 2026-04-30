package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.ReviewResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Review;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.entities.User;

public class ReviewMapper {

    private ReviewMapper() {}

    public static ReviewResponse toResponse(Review review, Long currentUserId) {

        if (review == null) {
            return null;
        }

        // ✅ đúng relation
        Student student = review.getStudent();
        User user = (student != null) ? student.getUser() : null;
        Course course = review.getCourse();

        return ReviewResponse.builder()
                .id(review.getId())

                // ===== USER =====
                .userId(user != null ? user.getId() : null)
                .userName(user != null ? user.getFullName() : null)

                // ===== COURSE =====
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)

                // ===== REVIEW =====
                .rating(review.getRating())
                .comment(review.getComment())

                // null-safe
                .isApproved(Boolean.TRUE.equals(review.getIsApproved()))

                // ✅ check ownership (quan trọng cho FE)
                .isMine(
                        currentUserId != null &&
                        user != null &&
                        currentUserId.equals(user.getId())
                )

                // ===== TIME =====
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())

                .build();
    }
}