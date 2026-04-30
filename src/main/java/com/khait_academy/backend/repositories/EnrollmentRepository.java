package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Enrollment;
import com.khait_academy.backend.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // ================= FIND =================

    List<Enrollment> findByStudent_Id(Long studentId);

    List<Enrollment> findByCourse_Id(Long courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    // ================= SINGLE =================

    Optional<Enrollment> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);
}