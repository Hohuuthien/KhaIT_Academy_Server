package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.AuthResponse;
import com.khait_academy.backend.dto.response.UserResponse;
import com.khait_academy.backend.entities.Role;
import com.khait_academy.backend.entities.User;

import java.util.List;

public class UserMapper {

    /**
     * ✅ Map User → AuthResponse
     */
    public static AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toList()
                )
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toList()
                )
                .createdAt(user.getCreatedAt())
                .build();
    }
    /**
     * ✅ Map User → List Role String
     */
    public static List<String> toRoleNames(User user) {
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();
    }
}