package com.khait_academy.backend.security;

import com.khait_academy.backend.exception.UnauthorizedException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityUtils {

    /**
     * Lấy Authentication từ SecurityContext
     */
    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthorized access attempt");
            throw new UnauthorizedException("Bạn chưa đăng nhập");
        }

        return authentication;
    }

    /**
     * Lấy UserPrincipal
     */
    private UserPrincipal getPrincipal() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal;
        }

        log.error("Invalid principal type: {}", principal.getClass().getName());
        throw new UnauthorizedException("Token không hợp lệ");
    }

    /**
     * Lấy userId (ưu tiên từ UserPrincipal)
     */
    public Long getCurrentUserId() {
        return getPrincipal().getId();
    }

    /**
     * Lấy email
     */
    public String getCurrentEmail() {
        Authentication authentication = getAuthentication();
        return authentication.getName(); // Spring Security chuẩn
    }

    /**
     * Check role
     */
    public boolean hasRole(String role) {
        Authentication authentication = getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }
}