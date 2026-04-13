package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    /**
     *  Tìm bài nộp theo user + assignment (dùng cho submit/update)
     */
    Optional<Submission> findByUserIdAndAssignmentId(Long userId, Long assignmentId);

    /**
     *  Lấy tất cả bài nộp theo assignment (FETCH JOIN tránh lazy)
     */
    @Query("""
        SELECT s FROM Submission s
        JOIN FETCH s.user
        JOIN FETCH s.assignment
        WHERE s.assignment.id = :assignmentId
    """)
    List<Submission> findByAssignmentId(Long assignmentId);

    /**
     *  Lấy tất cả bài nộp theo user
     */
    @Query("""
        SELECT s FROM Submission s
        JOIN FETCH s.assignment
        WHERE s.user.id = :userId
    """)
    List<Submission> findByUserId(Long userId);
}