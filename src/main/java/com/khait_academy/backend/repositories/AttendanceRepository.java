package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // ================= SINGLE =================

    @EntityGraph(attributePaths = {"user", "lesson"})
    Optional<Attendance> findByUser_IdAndLesson_Id(Long userId, Long lessonId);

    // ================= LIST =================

    @EntityGraph(attributePaths = {"user", "lesson"})
    List<Attendance> findByLesson_Id(Long lessonId);

    @EntityGraph(attributePaths = {"user", "lesson"})
    List<Attendance> findByUser_Id(Long userId);

    // ================= PAGINATION (QUAN TRỌNG) =================

    @EntityGraph(attributePaths = {"user", "lesson"})
    Page<Attendance> findByLesson_Id(Long lessonId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "lesson"})
    Page<Attendance> findByUser_Id(Long userId, Pageable pageable);

    // ================= DELETE =================

    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.lesson.id = :lessonId")
    void deleteByLessonId(@Param("lessonId") Long lessonId);
}