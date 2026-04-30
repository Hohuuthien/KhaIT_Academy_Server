package com.khait_academy.backend.security;

import com.khait_academy.backend.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String fullName; // ✅ thêm field cần thiết
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Long id,
                          String email,
                          String passwordHash,
                          String fullName,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.authorities = authorities;
    }

    // =========================
    // FACTORY METHOD
    // =========================
    public static UserPrincipal create(User user) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {

                // ROLE
                authorities.add(
                        new SimpleGrantedAuthority(role.getName().name())
                );

                // PERMISSIONS
                if (role.getPermissions() != null) {
                    role.getPermissions().forEach(p ->
                            authorities.add(
                                    new SimpleGrantedAuthority(p.getName())
                            )
                    );
                }
            });
        }

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getFullName(),
                authorities
        );
    }

    // =========================
    // SPRING SECURITY
    // =========================

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}