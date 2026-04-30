package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Order;
import com.khait_academy.backend.enums.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ================= USER =================

    /**
     * 👉 Orders của user (pagination)
     */
    @EntityGraph(attributePaths = {"items", "items.course"})
    Page<Order> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 👉 Filter theo status
     */
    @EntityGraph(attributePaths = {"items", "items.course"})
    Page<Order> findByUser_IdAndStatus(
            Long userId,
            OrderStatus status,
            Pageable pageable
    );

    // ================= DETAIL =================

    /**
     * 👉 Lấy 1 order (fetch full)
     */
    @EntityGraph(attributePaths = {"items", "items.course", "user"})
    Optional<Order> findWithDetailsById(Long id);

    // ================= COUNT =================

    long countByUser_Id(Long userId);

    long countByUser_IdAndStatus(Long userId, OrderStatus status);

    long countByStatus(OrderStatus status);

    // ================= ADMIN =================

    /**
     * 👉 Admin xem tất cả (pagination)
     */
    @EntityGraph(attributePaths = {"items", "items.course", "user"})
    Page<Order> findAll(Pageable pageable);

    // ================= FILTER (PRO) =================

    /**
     * 👉 Filter dynamic (admin dashboard)
     */
    @Query("""
        SELECT o FROM Order o
        WHERE (:userId IS NULL OR o.user.id = :userId)
        AND (:status IS NULL OR o.status = :status)
    """)
    Page<Order> filterOrders(
            Long userId,
            OrderStatus status,
            Pageable pageable
    );
}