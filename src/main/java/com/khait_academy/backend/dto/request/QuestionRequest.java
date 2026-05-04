package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.QuestionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {

    // ================= QUIZ =================

    @NotNull(message = "quizId không được để trống")
    private Long quizId;

    // ================= CONTENT =================

    @NotBlank(message = "content không được để trống")
    private String content;

    // ================= TYPE =================
    // Optional -> default MULTIPLE_CHOICE

    private QuestionType type = QuestionType.MULTIPLE_CHOICE;

    // ================= SCORE =================
    // Optional -> default 1

    @Min(value = 1, message = "score phải >= 1")
    private Integer score = 1;

    // ================= OPTIONS =================

    @Valid
    private List<QuestionOptionRequest> options;

    // ================= CORRECT ANSWERS =================

    private List<Integer> correctIndexes;
}