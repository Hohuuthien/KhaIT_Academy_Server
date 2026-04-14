package com.khait_academy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.khait_academy.backend.enums.SubmissionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // bỏ field null khi trả về JSON
public class SubmissionResponse {

    private Long id;

    private Long userId;
    private String userName;

    private Long assignmentId;
    private String assignmentTitle;

    private String fileUrl;

    private Double score;     // nullable (chưa chấm)
    private String feedback;  // nullable

    private SubmissionStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}