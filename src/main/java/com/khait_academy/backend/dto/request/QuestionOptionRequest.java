package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionOptionRequest {

    @NotBlank(message = "Option content không được để trống")
    @Size(max = 500, message = "Option content tối đa 500 ký tự")
    private String content;

    private boolean correct;
}