package com.khait_academy.backend.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.base64:true}") // mặc định dùng base64
    private boolean isBase64;

    @Bean
    public SecretKey jwtSigningKey() {

        byte[] keyBytes;

        if (isBase64) {
            // ✅ decode base64
            keyBytes = Decoders.BASE64.decode(jwtSecret);
        } else {
            // ✅ fallback: raw string
            keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        }

        // 🔥 CHECK CHUẨN HS256
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must be at least 256 bits (32 bytes). Current: " + keyBytes.length + " bytes"
            );
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}