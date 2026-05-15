package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
@ToString(exclude = {"order", "course"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ================= RELATION =================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    // ================= SNAPSHOT =================

    @NotBlank
    @Column(name = "course_title", nullable = false, length = 255)
    private String courseTitle;

    @Column(name = "course_thumbnail", length = 500)
    private String courseThumbnail;

    // ================= PRICE =================

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    // ================= FACTORY =================

    public static OrderItem fromCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        return OrderItem.builder()
                .course(course)
                .courseTitle(course.getTitle())
                .courseThumbnail(course.getThumbnail())
                .price(course.getPrice())
                .totalPrice(course.getPrice())
                .build();
    }

    // ================= BUSINESS METHODS =================

    public boolean isFree() {
        return price != null &&
               BigDecimal.ZERO.compareTo(price) == 0;
    }

    // ================= LIFECYCLE =================

    @PrePersist
    @PreUpdate
    public void prePersist() {

        if (this.price == null) {
            this.price = BigDecimal.ZERO;
        }

        if (this.totalPrice == null) {
            this.totalPrice = this.price;
        }
    }
}