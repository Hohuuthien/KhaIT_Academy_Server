package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserIdAndLessonId(Long userId, Long lessonId);

    List<Attendance> findByLessonId(Long lessonId);

    List<Attendance> findByUserId(Long userId);

    void deleteByLessonId(Long lessonId);
}