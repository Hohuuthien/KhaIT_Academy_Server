package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // ================= BASIC =================

    Page<Assignment> findByLessonId(Long lessonId, Pageable pageable);

    // ================= PUBLISHED =================

    Page<Assignment> findByIsPublishedTrue(Pageable pageable);

    Page<Assignment> findByLessonIdAndIsPublishedTrue(Long lessonId, Pageable pageable);

}