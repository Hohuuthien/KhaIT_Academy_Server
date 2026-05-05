package com.khait_academy.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.khait_academy.backend.enums.DiscountType;

@Entity
@Table(
    name = "discounts",
    indexes = {
        @Index(name = "idx_discount_course", columnList = "course_id"),
        @Index(name = "idx_discount_time", columnList = "start_date, end_date"),
        @Index(name = "idx_discount_active", columnList = "is_active")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== RELATION =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // ===== DISCOUNT INFO =====
    @NotNull(message = "Discount value không được null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount phải > 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @NotNull(message = "Discount type không được null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType type;

    // ===== TIME =====
    @NotNull(message = "startDate không được null")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "endDate không được null")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    // ===== STATUS =====
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; 

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ================= LIFECYCLE =================
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        validateLogic();
    }

    @PreUpdate
    public void preUpdate() {
        validateLogic();
    }

    // ================= BUSINESS VALIDATION =================
    private void validateLogic() {

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate phải <= endDate");
        }

        if (value == null) {
            throw new IllegalArgumentException("Discount value không được null");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount phải > 0");
        }

        if (type == DiscountType.PERCENT &&
                value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Discount % không được > 100");
        }
    }

    // ================= HELPER =================
    public boolean isValidNow() {
        LocalDateTime now = LocalDateTime.now();

        return Boolean.TRUE.equals(isActive)
                && !startDate.isAfter(now)
                && !endDate.isBefore(now);
    }
}