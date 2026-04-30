package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
    name = "order_items",
    indexes = {
        @Index(name = "idx_item_order", columnList = "order_id"),
        @Index(name = "idx_item_course", columnList = "course_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_order_course",
            columnNames = {"order_id", "course_id"}
        )
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== RELATION =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // ===== SNAPSHOT =====

    @Column(name = "course_title", nullable = false, length = 255)
    private String courseTitle;

    @Column(name = "course_thumbnail", length = 500)
    private String courseThumbnail;

    // ===== PRICE =====

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price; // giá tại thời điểm mua

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    // ===== HELPER =====

    @PrePersist
    public void prePersist() {
        if (this.totalPrice == null) {
            this.totalPrice = this.price;
        }
    }
}