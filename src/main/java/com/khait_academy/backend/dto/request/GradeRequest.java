package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeRequest {

    @Min(0)
    @Max(10)
    private Double score;

    private String feedback;
}