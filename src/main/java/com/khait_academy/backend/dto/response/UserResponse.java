package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserResponse {

    // ===== BASIC INFO =====
    private Long id;
    private String email;
    private String fullName;
    private String avatarUrl;

    // ===== STATUS =====
    private String status; // ACTIVE / INACTIVE / LOCKED (align với UserStatus enum)

    // ===== AUTH =====
    private List<String> roles;

    // ===== AUDIT =====
    private LocalDateTime createdAt;
}