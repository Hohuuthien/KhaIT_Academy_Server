package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.DiscountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponse {

    private Long id;

    // course info (flatten)
    private Long courseId;
    private String courseTitle;

    private BigDecimal value;
    private DiscountType type;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean isActive;
    private Boolean isValidNow;

    private LocalDateTime createdAt;
}