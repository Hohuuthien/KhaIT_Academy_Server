package com.khait_academy.backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

import com.khait_academy.backend.entities.OtpCode;

public interface OtpRepository extends JpaRepository<OtpCode, Long> {

    // ================= RATE LIMIT =================
    Optional<OtpCode> findTopByEmailOrderByCreatedAtDesc(String email);

    // ================= VALIDATE OTP =================
    Optional<OtpCode> findByEmailAndCodeAndUsedFalseAndExpiryTimeAfter(
            String email,
            String code,
            LocalDateTime now
    );

    // ================= LOCK (OPTIONAL - HIGH CONCURRENCY) =================
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<OtpCode> findByEmailAndCodeAndUsedFalse(
            String email,
            String code
    );

    // ================= INVALIDATE =================
    Optional<OtpCode> findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);

    // ================= CLEANUP =================
    void deleteByExpiryTimeBefore(LocalDateTime time);

    // ================= CHECK =================
    boolean existsByEmailAndCodeAndUsedFalse(String email, String code);
}