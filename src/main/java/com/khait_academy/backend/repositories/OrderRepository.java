package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Order;
import com.khait_academy.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByTransactionId(String transactionId);
}