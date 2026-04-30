package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    boolean existsByUserId(Long userId);

    Optional<Parent> findByUserId(Long userId);
}