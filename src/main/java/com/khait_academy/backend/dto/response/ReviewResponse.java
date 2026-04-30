package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Long id;

    // ===== USER =====
    private Long userId;
    private String userName;

    // ===== COURSE =====
    private Long courseId;
    private String courseTitle;

    // ===== REVIEW =====
    private Integer rating;
    private String comment;

    @Builder.Default
    private Boolean isApproved = false;

    // 👉 FE check review của chính mình
    private Boolean isMine;

    // ===== TIME =====
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}