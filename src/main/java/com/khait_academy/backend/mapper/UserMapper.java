package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.AuthResponse;
import com.khait_academy.backend.dto.response.UserResponse;
import com.khait_academy.backend.entities.User;

import java.util.List;

public class UserMapper {

    // =========================
    // USER → AUTH RESPONSE
    // =========================
    public static AuthResponse toAuthResponse(User user, String accessToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(toRoleNames(user))
                .build();
    }

    // =========================
    // USER → USER RESPONSE
    // =========================
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus().name()) // nếu bạn dùng UserStatus enum
                .roles(toRoleNames(user))
                .createdAt(user.getCreatedAt())
                .build();
    }

    // =========================
    // ROLE MAPPING
    // =========================
    public static List<String> toRoleNames(User user) {
        return user.getRoles()
                .stream()
                .map(role -> role.getName().name()) // ✅ FIX QUAN TRỌNG
                .toList();
    }
}