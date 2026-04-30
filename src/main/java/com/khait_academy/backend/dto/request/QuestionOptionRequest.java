package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionOptionRequest {

    // ===== CONTENT =====
    @NotBlank(message = "Option content không được để trống")
    private String content;

    // ===== CORRECT FLAG =====
    // dùng primitive để tránh null bug
    private boolean isCorrect;
}