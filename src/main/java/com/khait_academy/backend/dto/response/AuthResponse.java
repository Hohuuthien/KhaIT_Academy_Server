package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {

    // ===== TOKEN =====
    private String accessToken;
    private String refreshToken;

    // cố định FE dùng "Bearer"
    @Builder.Default
    private String tokenType = "Bearer";

    // ===== USER INFO =====
    private Long userId;
    private String email;
    private String fullName;

    // ===== AUTH =====
    private List<String> roles;
}