package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.DiscountType;
import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {

    @NotNull(message = "CourseId không được null")
    private Long courseId;

    @NotNull(message = "Value không được null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount phải > 0")
    private BigDecimal value;

    @NotNull(message = "Type không được null")
    private DiscountType type;

    @NotNull(message = "startDate không được null")
    private LocalDateTime startDate;

    @NotNull(message = "endDate không được null")
    private LocalDateTime endDate;

    private Boolean isActive;
}