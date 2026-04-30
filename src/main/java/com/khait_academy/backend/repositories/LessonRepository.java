package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseIdOrderByOrderIndexAsc(Long courseId);

    Optional<Lesson> findBySlug(String slug);

    boolean existsBySlug(String slug);
}