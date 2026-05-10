package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Parent;
import com.khait_academy.backend.enums.ParentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    // ================= EXISTENCE =================
    boolean existsByUserId(Long userId);

    // ================= FIND =================
    Optional<Parent> findByUserId(Long userId);

    // ================= FILTER =================
    Page<Parent> findByStatus(ParentStatus status, Pageable pageable);
}