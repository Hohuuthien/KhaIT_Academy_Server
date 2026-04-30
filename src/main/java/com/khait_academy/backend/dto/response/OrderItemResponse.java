package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private Long courseId;
    private String courseTitle;
    private BigDecimal price;
}