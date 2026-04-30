package com.khait_academy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.khait_academy.backend.enums.SubmissionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmissionResponse {

    // ===== IDENTIFIER =====
    private Long id;

    // ===== STUDENT =====
    private Long studentId;
    private String studentName;

    // ===== ASSIGNMENT =====
    private Long assignmentId;
    private String assignmentTitle;

    // ===== DATA =====
    private String fileUrl;

    private BigDecimal score;     // nullable (chưa chấm)
    private String feedback;      // nullable

    private SubmissionStatus status;

    // ===== TIME =====
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gradedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}