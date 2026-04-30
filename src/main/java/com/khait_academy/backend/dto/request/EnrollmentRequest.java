package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EnrollmentRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal priceAtPurchase;

    private Integer progress;

    private String status;

    private LocalDateTime expiredAt;
}