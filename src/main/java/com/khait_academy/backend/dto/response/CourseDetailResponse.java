package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CourseDetailResponse {

    // ===== BASIC =====
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String thumbnail;

    // ===== PRICE =====
    private BigDecimal originalPrice;
    private BigDecimal finalPrice;

    // ===== DISCOUNT =====
    private Boolean hasDiscount;
    private DiscountResponse discount;

    // ===== CATEGORY =====
    private Long categoryId;
    private String categoryName;

    // ===== TEACHER =====
    private Long teacherId;
    private String teacherName;

    // ===== CONTENT =====
    private List<LessonResponse> lessons;
    private List<ReviewResponse> reviews;

    // ===== STATS =====
    private Integer totalLessons;
    private Integer totalStudents;
    private BigDecimal averageRating;

    // ===== STATUS =====
    private String status;

    // ===== AUDIT =====
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}