package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttendanceResponse {

    private Long id;

    // student
    private Long studentId;
    private String studentName;

    // lesson
    private Long lessonId;
    private String lessonTitle;

    // attendance
    private AttendanceStatus status;
    private LocalDateTime attendedAt;
    private String note;

    // meta
    private Long checkedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}