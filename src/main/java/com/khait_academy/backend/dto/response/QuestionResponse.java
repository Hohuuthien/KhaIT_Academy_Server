package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.QuestionType;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionResponse {

    // ================= IDENTIFIER =================

    private Long id;
    private Long quizId;

    // ================= CONTENT =================

    private String content;
    private QuestionType type;
    private Integer score;

    // ================= OPTIONS =================

    private List<QuestionOptionResponse> options;
}