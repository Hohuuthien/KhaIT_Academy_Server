package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.LoginRequest;
import com.khait_academy.backend.dto.request.RegisterRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.AuthResponse;
import com.khait_academy.backend.services.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     *  REGISTER
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Register successfully")
                        .data(response)
                        .build()
        );
    }

    /**
     *  LOGIN
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login successfully")
                        .data(response)
                        .build()
        );
    }

    /**
     * GET CURRENT USER (từ JWT)
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> me(Authentication authentication) {

        // lấy email từ token (đã set trong SecurityContext)
        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Get current user success")
                        .data(email)
                        .build()
        );
    }
}