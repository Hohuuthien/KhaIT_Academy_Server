package com.khait_academy.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Lấy header Authorization
            String authHeader = request.getHeader("Authorization");

            // 2. Check header hợp lệ
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Cắt token
            String token = authHeader.substring(7);

            // 4. Validate token
            if (!jwtTokenProvider.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 5. Lấy email từ token (theo method của bạn)
            String email = jwtTokenProvider.getEmailFromToken(token);

            // 6. Nếu chưa có authentication
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 7. Load user từ DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 8. Tạo authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 🔥 QUAN TRỌNG NHẤT
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception ex) {
            // log lỗi
            System.out.println("JWT Filter error: " + ex.getMessage());
        }

        // 9. tiếp tục filter chain
        filterChain.doFilter(request, response);
    }
}