package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khait_academy.backend.enums.OrderStatus;
import com.khait_academy.backend.enums.PaymentMethod;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "orders",
    indexes = {
        @Index(name = "idx_order_user", columnList = "user_id"),
        @Index(name = "idx_order_status", columnList = "status"),
        @Index(name = "idx_order_created", columnList = "created_at"),
        @Index(name = "idx_order_transaction", columnList = "transaction_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_order_transaction",
            columnNames = "transaction_id"
        )
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "items"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ================= USER =================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // ================= PRICE =================

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;

    // ================= STATUS =================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    // ================= PAYMENT =================

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    // ================= TIME =================

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ================= RELATION =================

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // ================= HELPER METHODS =================

    public void addItem(OrderItem item) {
        if (item == null) return;

        items.add(item);
        item.setOrder(this);

        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        if (item == null) return;

        items.remove(item);
        item.setOrder(null);

        recalculateTotal();
    }

    public void clearItems() {
        items.forEach(item -> item.setOrder(null));
        items.clear();

        recalculateTotal();
    }

    public void recalculateTotal() {
        this.totalPrice = items.stream()
                .map(OrderItem::getTotalPrice)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ================= BUSINESS METHODS =================

    public void markPaid(String transactionId) {

        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                "Cancelled order cannot be paid"
            );
        }

        this.status = OrderStatus.PAID;
        this.transactionId = transactionId;
    }

    public void cancel() {

        if (this.status == OrderStatus.PAID) {
            throw new IllegalStateException(
                "Paid order cannot be cancelled"
            );
        }

        this.status = OrderStatus.CANCELLED;
    }

    public boolean isPaid() {
        return this.status == OrderStatus.PAID;
    }

    public boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    public boolean isCancelled() {
        return this.status == OrderStatus.CANCELLED;
    }
}