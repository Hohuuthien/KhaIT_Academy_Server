package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long lessonId;

    @NotNull
    private AttendanceStatus status;

    private LocalDateTime attendedAt;

    private String note;

    private Long checkedBy;
}