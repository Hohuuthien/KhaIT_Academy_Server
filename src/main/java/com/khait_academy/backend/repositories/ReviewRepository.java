package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // ================= BASIC =================

    boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    Optional<Review> findByIdAndStudent_Id(Long id, Long studentId);

    // ================= GET REVIEWS =================

    Page<Review> findByCourse_IdAndIsApprovedTrue(Long courseId, Pageable pageable);

    // ================= FETCH (ANTI N+1) =================

    @EntityGraph(attributePaths = {"student", "course"})
    Page<Review> findWithStudentAndCourseByCourse_IdAndIsApprovedTrue(
            Long courseId,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"student", "course"})
    List<Review> findByCourse_IdAndIsApprovedTrue(Long courseId);

    // ================= AVG =================

    @Query("""
        SELECT COALESCE(AVG(r.rating), 0)
        FROM Review r
        WHERE r.course.id = :courseId AND r.isApproved = true
    """)
    Double getAverageRating(@Param("courseId") Long courseId);

    // ================= COUNT =================

    @Query("""
        SELECT COUNT(r)
        FROM Review r
        WHERE r.course.id = :courseId AND r.isApproved = true
    """)
    Long countApproved(@Param("courseId") Long courseId);

    // ================= SORT =================

    Page<Review> findByCourse_IdAndIsApprovedTrueOrderByCreatedAtDesc(
            Long courseId, Pageable pageable
    );

    Page<Review> findByCourse_IdAndIsApprovedTrueOrderByRatingDesc(
            Long courseId, Pageable pageable
    );

    Page<Review> findByCourse_IdAndIsApprovedTrueOrderByRatingAsc(
            Long courseId, Pageable pageable
    );

    // ================= STATS =================

    interface RatingStats {
        Integer getRating();
        Long getTotal();
    }

    @Query("""
        SELECT r.rating as rating, COUNT(r) as total
        FROM Review r
        WHERE r.course.id = :courseId AND r.isApproved = true
        GROUP BY r.rating
    """)
    List<RatingStats> getRatingStats(@Param("courseId") Long courseId);
}