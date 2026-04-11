package com.khait_academy.backend.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    /**
     *  Bean dùng chung cho toàn hệ thống (TokenProvider, Filter, ...)
     */
    @Bean
    public SecretKey jwtSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}