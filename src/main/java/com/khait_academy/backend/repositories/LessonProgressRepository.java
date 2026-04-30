package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    Optional<LessonProgress> findByStudentIdAndLessonId(Long studentId, Long lessonId);

    List<LessonProgress> findByStudentId(Long studentId);

    List<LessonProgress> findByLessonId(Long lessonId);
}