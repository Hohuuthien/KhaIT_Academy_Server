package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Submission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    /**
     * ✅ Tìm submission theo user + assignment (UPSERT)
     */
    @EntityGraph(attributePaths = {"user", "assignment"})
    Optional<Submission> findByUser_IdAndAssignment_Id(Long userId, Long assignmentId);

    /**
     * ✅ Lấy danh sách theo assignment (có pagination)
     */
    @EntityGraph(attributePaths = {"user", "assignment"})
    Page<Submission> findByAssignment_Id(Long assignmentId, Pageable pageable);

    /**
     * ✅ Lấy danh sách theo user (có pagination)
     */
    @EntityGraph(attributePaths = {"assignment"})
    Page<Submission> findByUser_Id(Long userId, Pageable pageable);

    /**
     * ✅ (Optional) Check tồn tại
     */
    boolean existsByUser_IdAndAssignment_Id(Long userId, Long assignmentId);
}