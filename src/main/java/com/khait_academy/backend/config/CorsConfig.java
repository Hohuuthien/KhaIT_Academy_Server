package com.khait_academy.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Cho phép frontend (React)
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",   // React local
                "http://localhost:5173"    // Vite
        ));

        // Cho phép method
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // Cho phép header
        config.setAllowedHeaders(List.of("*"));

        // Cho phép gửi token (Authorization)
        config.setAllowCredentials(true);

        //Expose header cho frontend đọc
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}