package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.CourseRequest;
import com.khait_academy.backend.dto.response.CourseResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.enums.CourseStatus;

import java.math.BigDecimal;

public class CourseMapper {

    // ================= CREATE =================
    public static Course toEntity(CourseRequest request) {

        CourseStatus status;

        try {
            status = (request.getStatus() != null)
                    ? CourseStatus.valueOf(request.getStatus().toUpperCase())
                    : CourseStatus.DRAFT;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid course status: " + request.getStatus());
        }

        return Course.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .description(request.getDescription())
                .price(request.getPrice())
                .thumbnail(request.getThumbnail())
                .level(request.getLevel())
                .duration(request.getDuration())
                .status(status)
                .build();
    }

    // ================= RESPONSE (FINAL PRICE SUPPORT) =================
    public static CourseResponse toResponse(Course course, BigDecimal finalPrice) {

        boolean hasDiscount =
                finalPrice != null &&
                course.getPrice() != null &&
                finalPrice.compareTo(course.getPrice()) < 0;

        return CourseResponse.builder()
                .id(course.getId())

                // BASIC
                .title(course.getTitle())
                .slug(course.getSlug())
                .description(course.getDescription())
                .thumbnail(course.getThumbnail())

                // PRICE
                .originalPrice(course.getPrice())
                .finalPrice(finalPrice != null ? finalPrice : course.getPrice())

                // DISCOUNT
                .hasDiscount(hasDiscount)
                .discount(null)

                // CATEGORY
                .categoryId(course.getCategory() != null ? course.getCategory().getId() : null)
                .categoryName(course.getCategory() != null ? course.getCategory().getName() : null)

                // TEACHER (FIX: KHÔNG DÙNG getFullName nếu entity không có)
                .teacherId(course.getTeacher() != null ? course.getTeacher().getId() : null)
                .teacherName(course.getTeacher() != null ? course.getTeacher().getEmail() : null)

                // REVIEW
                .averageRating(course.getAverageRating())
                .totalReviews(course.getTotalReviews())

                // STATS
                .totalLessons(course.getLessons() != null ? course.getLessons().size() : 0)
                .totalStudents(course.getEnrollments() != null ? course.getEnrollments().size() : 0)
                .isFree(course.getPrice() != null &&
                        course.getPrice().compareTo(BigDecimal.ZERO) == 0)

                // STATUS
                .status(course.getStatus() != null ? course.getStatus().name() : null)

                // AUDIT
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())

                .build();
    }

    // ================= UPDATE =================
    public static void updateEntity(Course course, CourseRequest request) {

        if (request.getTitle() != null) course.setTitle(request.getTitle());
        if (request.getSlug() != null) course.setSlug(request.getSlug());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getPrice() != null) course.setPrice(request.getPrice());
        if (request.getThumbnail() != null) course.setThumbnail(request.getThumbnail());
        if (request.getLevel() != null) course.setLevel(request.getLevel());
        if (request.getDuration() != null) course.setDuration(request.getDuration());

        if (request.getStatus() != null) {
            course.setStatus(CourseStatus.valueOf(request.getStatus().toUpperCase()));
        }
    }
}