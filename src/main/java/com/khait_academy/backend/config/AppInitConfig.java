package com.khait_academy.backend.config;

import com.khait_academy.backend.dto.request.RegisterRequest;
import com.khait_academy.backend.repositories.UserRepository;
import com.khait_academy.backend.services.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("dev") 
public class AppInitConfig {

    private final AuthService authService;
    private final UserRepository userRepository;

    private static final List<String> DEFAULT_USERS =
            List.of("anhminhnguyen19102004");

    @Bean
    ApplicationRunner initData() {
        return args -> {

            for (String name : DEFAULT_USERS) {

                String email = name + "@gmail.com";

                // tránh duplicate
                if (userRepository.existsByEmail(email)) continue;

                RegisterRequest request = new RegisterRequest();
                request.setEmail(email);
                request.setPassword("123456"); // nên dùng env
                request.setFullName(name);

                authService.register(request);
            }
        };
    }
}