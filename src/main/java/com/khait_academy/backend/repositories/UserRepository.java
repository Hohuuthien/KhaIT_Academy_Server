package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    List<User> findAll();

    // =========================
    // AUTH LOOKUP (FULL RBAC)
    // =========================
    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    Optional<User> findByEmail(String email);

    // =========================
    // EXISTS CHECK
    // =========================
    boolean existsByEmail(String email);

    // =========================
    // LOAD USER BY ID (PROFILE)
    // =========================
    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    Optional<User> findUserWithRolesAndPermissionsById(Long id);
}