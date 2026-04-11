package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUser_Id(Long userId);

    List<Enrollment> findByUser_IdOrderByEnrolledAtDesc(Long userId);
    
    Optional<Enrollment> findByUser_IdAndCourse_Id(Long userId, Long courseId);

    boolean existsByUser_IdAndCourse_Id(Long userId, Long courseId);
}