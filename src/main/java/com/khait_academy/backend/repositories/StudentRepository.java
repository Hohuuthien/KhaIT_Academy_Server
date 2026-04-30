package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByUserId(Long userId);

    Optional<Student> findByUserId(Long userId);
    
    Optional<Student> findByUser_Email(String email);
}