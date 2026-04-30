package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequest {

    // ===== ASSIGNMENT =====
    @NotNull(message = "assignmentId không được để trống")
    private Long assignmentId;

    // ===== FILE =====
    @NotBlank(message = "fileUrl không được để trống")
    @Size(max = 500, message = "fileUrl tối đa 500 ký tự")
    private String fileUrl;
}