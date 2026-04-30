package com.khait_academy.backend.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khait_academy.backend.entities.OtpCode;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.repositories.OtpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int OTP_EXPIRE_MINUTES = 5;
    private static final int OTP_RESEND_SECONDS = 30;

    // ================= GENERATE =================
    @Transactional
    public void generateOtp(String email) {

        LocalDateTime now = LocalDateTime.now();

        // ✔️ rate limit chuẩn (dựa vào createdAt)
       otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
    .ifPresent(latest -> {

        if (latest.getCreatedAt() != null &&
            latest.getCreatedAt().plusSeconds(OTP_RESEND_SECONDS).isAfter(now)) {

            throw new BadRequestException("Please wait before requesting another OTP");
        }
    });

        String otp = String.valueOf(100000 + RANDOM.nextInt(900000));

        OtpCode entity = OtpCode.builder()
                .email(email)
                .code(otp)
                .createdAt(now)
                .expiryTime(now.plusMinutes(OTP_EXPIRE_MINUTES))
                .used(false)
                .build();

        otpRepository.save(entity);

        sendEmail(email, otp);
    }

    // ================= VALIDATE =================
    @Transactional
    public void validateOtp(String email, String otp) {

        // ✔️ query chuẩn: email + code + chưa dùng + chưa hết hạn
        OtpCode entity = otpRepository
                .findByEmailAndCodeAndUsedFalseAndExpiryTimeAfter(
                        email,
                        otp,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new BadRequestException("Invalid or expired OTP"));

        // ✔️ mark used (atomic)
        entity.setUsed(true);
        otpRepository.save(entity);
    }

    // ================= SEND EMAIL =================
    private void sendEmail(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP Verification");
        message.setText("""
                Your OTP is: %s
                Expiry: %d minutes
                """.formatted(otp, OTP_EXPIRE_MINUTES));

        mailSender.send(message);
    }

    // ================= INVALIDATE =================
    @Transactional
    public void invalidateOtp(String email) {

        otpRepository
                .findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email)
                .ifPresent(otp -> {
                    otp.setUsed(true);
                    otpRepository.save(otp);
                });
    }
}