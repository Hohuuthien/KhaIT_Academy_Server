package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.LoginRequest;
import com.khait_academy.backend.dto.request.RegisterRequest;
import com.khait_academy.backend.dto.response.AuthResponse;
import com.khait_academy.backend.entities.Role;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.RoleName;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.repositories.RoleRepository;
import com.khait_academy.backend.repositories.UserRepository;
import com.khait_academy.backend.security.JwtTokenProvider;
import com.khait_academy.backend.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    private final OtpService otpService;

    // ================= REGISTER (SEND OTP) =================
    public void register(RegisterRequest request) {

        String email = request.getEmail();

        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is required");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }

        otpService.generateOtp(email);
    }

    // ================= VERIFY OTP + CREATE USER =================
    @Transactional
    public AuthResponse verifyOtpAndCreateUser(
            String email,
            String otp,
            String password,
            String fullName
    ) {

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("User already exists");
        }

        otpService.validateOtp(email, otp);

        Role role = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found: ROLE_STUDENT")
                );

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .fullName(fullName)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);

        otpService.invalidateOtp(email); // tránh reuse OTP

        String token = generateToken(user);

        return buildAuthResponseFromUser(user, token);
    }

    // ================= LOGIN =================
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String token = jwtTokenProvider.generateAccessToken(authentication);

        return buildAuthResponseFromPrincipal(principal, token);
    }

    // ================= FORGOT PASSWORD =================
    public void forgotPassword(String email) {

        if (!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("User not found");
        }

        otpService.generateOtp(email);
    }

    // ================= RESET PASSWORD =================
    @Transactional
    public void resetPassword(String email, String otp, String newPassword) {

        otpService.validateOtp(email, otp);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        otpService.invalidateOtp(email); // tránh reuse OTP
    }

    // ================= PRIVATE =================

    private String generateToken(User user) {

        UserPrincipal principal = UserPrincipal.create(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );

        return jwtTokenProvider.generateAccessToken(authentication);
    }

    private AuthResponse buildAuthResponseFromUser(User user, String token) {

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
                                .map(Enum::name)
                                .toList()
                )
                .build();
    }

    private AuthResponse buildAuthResponseFromPrincipal(UserPrincipal principal, String token) {

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(principal.getId())
                .email(principal.getEmail())
                .fullName(principal.getFullName())
                .roles(
                        principal.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .build();
    }
}