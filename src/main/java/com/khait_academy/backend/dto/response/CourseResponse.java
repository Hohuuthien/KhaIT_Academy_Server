package com.khait_academy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CourseResponse {

    private Long id;

    // ===== BASIC =====
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

    // ===== TEACHER (FIXED) =====
    private Long teacherId;
    private String teacherName;

    // ===== REVIEW =====
    private BigDecimal averageRating;
    private Integer totalReviews;

    // ===== STATS (computed in service) =====
    private Integer totalLessons;
    private Integer totalStudents;
    private Boolean isFree;

    // ===== STATUS =====
    private String status; // CourseStatus enum -> String (safe for FE)

    // ===== AUDIT =====
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}