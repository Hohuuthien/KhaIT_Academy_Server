package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Submission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // ================= UPSERT =================
    @EntityGraph(attributePaths = {"student", "assignment"})
    Optional<Submission> findByStudent_IdAndAssignment_Id(Long studentId, Long assignmentId);

    // ================= BY ASSIGNMENT =================
    @EntityGraph(attributePaths = {"student", "assignment"})
    @Query("""
        SELECT s FROM Submission s
        WHERE s.assignment.id = :assignmentId
        ORDER BY s.submittedAt DESC
    """)
    Page<Submission> findByAssignment_Id(Long assignmentId, Pageable pageable);

    // ================= BY STUDENT =================
    @EntityGraph(attributePaths = {"assignment"})
    @Query("""
        SELECT s FROM Submission s
        WHERE s.student.id = :studentId
        ORDER BY s.submittedAt DESC
    """)
    Page<Submission> findByStudent_Id(Long studentId, Pageable pageable);

    // ================= SINGLE =================
    @EntityGraph(attributePaths = {"student", "assignment"})
    Optional<Submission> findById(Long id);

    // ================= EXISTS =================
    boolean existsByStudent_IdAndAssignment_Id(Long studentId, Long assignmentId);

    // ================= COUNT (optional nhưng rất hữu ích) =================
    long countByAssignment_Id(Long assignmentId);

    long countByStudent_Id(Long studentId);
}