package com.khait_academy.backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;

    private Long courseId;

    private String courseTitle;

    private String courseThumbnail;

    private BigDecimal price;

    private BigDecimal totalPrice;
}