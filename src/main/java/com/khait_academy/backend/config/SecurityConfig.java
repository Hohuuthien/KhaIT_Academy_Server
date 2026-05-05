package com.khait_academy.backend.config;

import com.khait_academy.backend.security.JwtAuthenticationFilter;
import com.khait_academy.backend.security.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.khait_academy.backend.constants.SecurityEndpoints.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // ================= PUBLIC =================
                .requestMatchers(PUBLIC).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()

                // ================= ENROLLMENT =================
                .requestMatchers(ENROLLMENT).authenticated()

                // ================= COURSE =================
                .requestMatchers(HttpMethod.POST, COURSE)
                    .hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, COURSE)
                    .hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, COURSE)
                    .hasRole("ADMIN")

                // ================= LESSON =================
                .requestMatchers(HttpMethod.POST, LESSON)
                    .hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, LESSON)
                    .hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, LESSON)
                    .hasRole("ADMIN")

                // ================= ASSIGNMENT =================
                .requestMatchers(HttpMethod.GET, ASSIGNMENT)
                    .authenticated() // student xem
                .requestMatchers(HttpMethod.POST, ASSIGNMENT)
                    .hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, ASSIGNMENT)
                    .hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, ASSIGNMENT)
                    .hasRole("ADMIN")

                // ================= DISCOUNT =================
                .requestMatchers(HttpMethod.GET, DISCOUNT)
                    .permitAll() // ai cũng xem được giá
                .requestMatchers(HttpMethod.POST, DISCOUNT)
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, DISCOUNT)
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, DISCOUNT)
                    .hasRole("ADMIN")

                // ================= TEACHER =================
                .requestMatchers(HttpMethod.GET, TEACHER)
                    .hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.POST, TEACHER)
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, TEACHER)
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, TEACHER)
                    .hasRole("ADMIN")

                // ================= ADMIN =================
                .requestMatchers(ADMIN).hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ================= AUTH PROVIDER =================
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // ================= PASSWORD =================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // ================= AUTH MANAGER =================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}