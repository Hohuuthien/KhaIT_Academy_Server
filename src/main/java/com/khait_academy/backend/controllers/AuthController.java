package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.*;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.AuthResponse;
import com.khait_academy.backend.security.UserPrincipal;
import com.khait_academy.backend.services.AuthService;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return response(HttpStatus.ACCEPTED,
                "OTP sent to your email",
                "Check your email to verify account");
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        AuthResponse response = authService.verifyOtpAndCreateUser(
                request.getEmail(),
                request.getOtp(),
                request.getPassword(),
                request.getFullName()
        );

        return response(HttpStatus.OK,
                "Account verified successfully",
                response);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return response(HttpStatus.OK,
                "Login successfully",
                response);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request.getEmail());

        return response(HttpStatus.OK,
                "OTP sent to email",
                "Check your email to reset password");
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );

        return response(HttpStatus.OK,
                "Password reset successfully",
                "You can now login with new password");
    }

    // ================= CURRENT USER =================
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> me(Authentication authentication) {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        AuthResponse response = AuthResponse.builder()
                .userId(principal.getId())
                .email(principal.getEmail())
                .fullName(principal.getFullName())
                .roles(
                        principal.getAuthorities()
                                .stream()
                                .map(a -> a.getAuthority())
                                .toList()
                )
                .build();

        return response(HttpStatus.OK,
                "Get current user success",
                response);
    }

    // ================= COMMON RESPONSE =================
    private <T> ResponseEntity<ApiResponse<T>> response(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(
                ApiResponse.<T>builder()
                        .success(true)
                        .message(message)
                        .data(data)
                        .build()
        );
    }
}