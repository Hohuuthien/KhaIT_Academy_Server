package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.QuestionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {

    // ===== QUIZ =====
    @NotNull(message = "quizId không được để trống")
    private Long quizId;

    // ===== CONTENT =====
    @NotBlank(message = "content không được để trống")
    private String content;

    // ===== TYPE =====
    @NotNull(message = "type không được để trống")
    private QuestionType type;

    // ===== SCORE =====
    @NotNull(message = "score không được để trống")
    @Min(value = 1, message = "score phải >= 1")
    private Integer score;

    // ===== OPTIONS =====
    private List<QuestionOptionRequest> options;

    // ===== CORRECT ANSWERS =====
    // 👉 dùng option index hoặc flag trong option (recommended)
    private List<Integer> correctIndexes;
}