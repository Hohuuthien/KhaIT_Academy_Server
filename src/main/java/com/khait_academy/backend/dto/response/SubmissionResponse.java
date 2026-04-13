package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.SubmissionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionResponse {

    private Long id;

    private Long userId;
    private String userName;

    private Long assignmentId;
    private String assignmentTitle;

    private String fileUrl;
    private Double score;
    private String feedback;
    private SubmissionStatus status;

    private LocalDateTime submittedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}