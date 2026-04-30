package com.khait_academy.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    // ===== CONFIG =====
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-expiration-ms}")
    private long accessTokenExpiration;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshTokenExpiration;

    private SecretKey key;

    // ===== CLAIM KEYS =====
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TYPE = "type";

    // ===== INIT =====
    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters");
        }

        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        log.info("JWT initialized successfully");
    }

    // ================= GENERATE TOKEN =================

    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return buildToken(
                userDetails.getUsername(),
                roles,
                accessTokenExpiration,
                "ACCESS"
        );
    }

    public String generateRefreshToken(String email) {
        return buildToken(
                email,
                null,
                refreshTokenExpiration,
                "REFRESH"
        );
    }

    private String buildToken(String subject,
                              List<String> roles,
                              long expiration,
                              String type) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        JwtBuilder builder = Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .claim(CLAIM_TYPE, type)
                .signWith(key);

        if (roles != null) {
            builder.claim(CLAIM_ROLES, roles);
        }

        return builder.compact();
    }

    // ================= PARSE TOKEN =================

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getClaims(token).get(CLAIM_ROLES, List.class);
    }

    public String getTokenType(String token) {
        return getClaims(token).get(CLAIM_TYPE, String.class);
    }

    // ================= VALIDATION =================

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("Invalid signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT empty: {}", e.getMessage());
        }
        return false;
    }
}