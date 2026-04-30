package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionOptionResponse {
    private Long id;
    private String content;
    private Boolean isCorrect;
}