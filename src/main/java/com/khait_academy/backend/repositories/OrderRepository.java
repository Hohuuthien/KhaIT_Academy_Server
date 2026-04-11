package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Order;
import com.khait_academy.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}