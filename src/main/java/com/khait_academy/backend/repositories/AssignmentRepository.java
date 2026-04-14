package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @EntityGraph(attributePaths = {"lesson"})
    Page<Assignment> findByLesson_Id(Long lessonId, Pageable pageable);
}