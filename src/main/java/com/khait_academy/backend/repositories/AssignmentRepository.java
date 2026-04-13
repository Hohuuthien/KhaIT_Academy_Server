package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByLessonId(Long lessonId);
}