package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.UserRequest;
import com.khait_academy.backend.dto.response.UserResponse;
import com.khait_academy.backend.entities.Role;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.RoleName;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.NotFoundException;
import com.khait_academy.backend.mapper.UserMapper;
import com.khait_academy.backend.repositories.RoleRepository;
import com.khait_academy.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // =========================
    // CREATE USER
    // =========================
    @Transactional
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Set<Role> roles = new HashSet<>();

        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {

            Role defaultRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                    .orElseThrow(() -> new NotFoundException("Default role not found"));

            roles.add(defaultRole);

        } else {

            roles = request.getRoleIds().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new NotFoundException("Role not found: " + roleId))
                    )
                    .collect(Collectors.toSet());
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .avatarUrl(request.getAvatarUrl())
                .roles(roles)
                .build();

        user = userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    // =========================
    // GET ALL
    // =========================
   @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserResponse)
                .toList();
    }

    // =========================
    // GET BY ID
    // =========================
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return UserMapper.toUserResponse(user);
    }

    // =========================
    // CURRENT USER
    // =========================
    public UserResponse getCurrentUser(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return UserMapper.toUserResponse(user);
    }

    // =========================
    // UPDATE
    // =========================
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setFullName(request.getFullName());
        user.setAvatarUrl(request.getAvatarUrl());

        user = userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    // =========================
    // DELETE
    // =========================
    @Transactional
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }
}