package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ParentRequest {

    @NotNull(message = "UserId is required")
    private Long userId;

    @Size(max = 150)
    private String fullName;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String address;

    private String status;
}