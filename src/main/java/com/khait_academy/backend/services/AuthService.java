package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.LoginRequest;
import com.khait_academy.backend.dto.request.RegisterRequest;
import com.khait_academy.backend.dto.response.AuthResponse;
import com.khait_academy.backend.entities.Role;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.repositories.RoleRepository;
import com.khait_academy.backend.repositories.UserRepository;
import com.khait_academy.backend.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * ✅ REGISTER
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // check email tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã được sử dụng: " + request.getEmail());
        }

        // lấy role mặc định
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role", "name", "ROLE_USER")
                );
        
        // tạo user
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        // tạo token
        String token = jwtTokenProvider.generateTokenFromEmail(user.getEmail());

        return buildAuthResponse(user, token);
    }

    /**
     * ✅ LOGIN
     */
    public AuthResponse login(LoginRequest request) {

        // authenticate (Spring sẽ tự check password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // tạo token
        String token = jwtTokenProvider.generateToken(authentication);

        // lấy user từ DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", request.getEmail())
                );

        return buildAuthResponse(user, token);
    }

    /**
     * ✅ BUILD RESPONSE
     */
    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .accessToken(token)   // ✅ dùng chuẩn này
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .build();
    }
}