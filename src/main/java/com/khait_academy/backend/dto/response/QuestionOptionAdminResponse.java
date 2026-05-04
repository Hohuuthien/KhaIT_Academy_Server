package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionOptionAdminResponse {

    private Long id;

    private String content;

    private boolean correct;
}