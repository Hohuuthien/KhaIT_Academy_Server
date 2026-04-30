package com.khait_academy.backend.security;

import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.UserStatus;
import com.khait_academy.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email)
                );

        // =========================
        // STATUS CHECK (SAFE)
        // =========================
        if (user.getStatus() == null) {
            throw new DisabledException("User status is null");
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new DisabledException("User is inactive");
        }

        if (user.getStatus() == UserStatus.LOCKED) {
            throw new LockedException("User is locked");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new DisabledException("User is not active");
        }

        return UserPrincipal.create(user);
    }
}