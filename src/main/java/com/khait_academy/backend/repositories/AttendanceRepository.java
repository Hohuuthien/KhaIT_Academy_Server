package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Attendance;
import com.khait_academy.backend.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // ================= PAGINATION =================
    Page<Attendance> findByStudentId(Long studentId, Pageable pageable);

    Page<Attendance> findByLessonId(Long lessonId, Pageable pageable);

    Page<Attendance> findByStatus(AttendanceStatus status, Pageable pageable);

    // ================= VALIDATION =================
    Optional<Attendance> findByStudentIdAndLessonId(Long studentId, Long lessonId);
}