package com.khait_academy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudentResponse {

    private Long id;

    // USER
    private Long userId;
    private String studentName;
    private String email;

    // PARENT
    private Long parentId;
    private String parentName;

    // PROFILE
    private String gradeLevel;
    private String schoolName;
    private String learningGoal;
    private Double averageScore;

    // STATUS
    private String status;

    // AUDIT
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}