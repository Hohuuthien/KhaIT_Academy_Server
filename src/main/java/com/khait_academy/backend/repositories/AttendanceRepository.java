package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Attendance;
import com.khait_academy.backend.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findByLessonId(Long lessonId);

    List<Attendance> findByStatus(AttendanceStatus status);

    Optional<Attendance> findByStudentIdAndLessonId(Long studentId, Long lessonId);
}