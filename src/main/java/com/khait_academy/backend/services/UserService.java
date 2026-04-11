package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.UserRequest;
import com.khait_academy.backend.dto.response.UserResponse;
import com.khait_academy.backend.entities.Role;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.mapper.UserMapper;
import com.khait_academy.backend.repositories.RoleRepository;
import com.khait_academy.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication; // ✅ FIX
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * ✅ CREATE USER
     */
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository
                        .findByName("ROLE_" + roleName.toUpperCase())
                        .orElseThrow(() ->
                                new RuntimeException("Role not found: " + roleName)
                        ))
                .collect(Collectors.toSet());

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .avatarUrl(request.getAvatarUrl())
                .roles(roles)
                .build();

        userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    /**
     * ✅ GET ALL USERS
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserResponse)
                .toList();
    }

    /**
     * ✅ GET USER BY ID
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toUserResponse(user);
    }

    /**
     * ✅ GET CURRENT USER
     */
    public UserResponse getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toUserResponse(user);
    }

    /**
     * ✅ UPDATE USER (không đổi password + role)
     */
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        user.setAvatarUrl(request.getAvatarUrl());

        userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    /**
     * ✅ DELETE USER
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}