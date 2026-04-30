package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherRequest {

    // ===== LINK USER =====
    @NotNull(message = "UserId is required")
    private Long userId;

    // ===== PROFILE =====
    @Size(max = 255)
    private String fullName;

    @Email(message = "Invalid email")
    private String email;

    @Size(max = 5000)
    private String bio;

    @Min(value = 0)
    private Integer experienceYears;

    @Size(max = 255)
    private String specialization;

    // ===== STATUS (optional admin) =====
    private String status;
}