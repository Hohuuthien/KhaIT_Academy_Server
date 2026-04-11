package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {

    private String accessToken;        // ✅ dùng cái này thôi
    private String tokenType;    // Bearer

    private Long userId;
    private String email;
    private String fullName;

    private List<String> roles;
}