package com.khait_academy.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ===== ORIGINS =====
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://*.khait-academy.com" // production domain
        ));

        // ===== METHODS =====
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // ===== HEADERS =====
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Requested-With"
        ));

        // ===== EXPOSE =====
        config.setExposedHeaders(List.of(
                "Authorization"
        ));

        // ===== CREDENTIAL =====
        config.setAllowCredentials(true);

        // ===== CACHE PREFLIGHT =====
        config.setMaxAge(3600L); // 1h

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}