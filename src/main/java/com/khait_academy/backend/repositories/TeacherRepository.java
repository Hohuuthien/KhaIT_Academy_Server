package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByUserId(Long userId);

    Optional<Teacher> findByUserId(Long userId);
}