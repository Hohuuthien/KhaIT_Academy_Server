package com.khait_academy.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.khait_academy.backend.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "payment_transactions",
    indexes = {
        @Index(name = "idx_txn_order", columnList = "order_id"),
        @Index(name = "idx_txn_status", columnList = "status"),
        @Index(name = "idx_txn_provider_ref", columnList = "provider_ref")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_provider_ref", columnNames = {"provider", "provider_ref"})
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== ORDER =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // ===== IDP KEY =====
    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    // ===== PROVIDER =====
    @Column(nullable = false)
    private String provider; // VNPAY | STRIPE

    @Column(name = "provider_ref", length = 255)
    private String providerRef;

    // ===== MONEY =====
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // ===== STATUS =====
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, SUCCESS, FAILED

    // ===== RAW DATA =====
    @Lob
    private String requestPayload;

    @Lob
    private String responsePayload;

    @Lob
    private String webhookPayload;

    // ===== TIME =====
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
