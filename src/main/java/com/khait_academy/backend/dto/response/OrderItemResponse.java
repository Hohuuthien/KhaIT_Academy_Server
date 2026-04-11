package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private Long courseId;
    private String courseTitle;
    private Double price;
}