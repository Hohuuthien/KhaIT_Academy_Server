package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    // =========================
    // BASIC
    // =========================
    Optional<Course> findBySlug(String slug);

    boolean existsBySlug(String slug);

    // =========================
    // FETCH JOIN (DETAIL)
    // =========================

    @Query("""
        SELECT c FROM Course c
        LEFT JOIN FETCH c.category
        LEFT JOIN FETCH c.teacher
        WHERE c.id = :id
    """)
    Optional<Course> findByIdWithRelations(@Param("id") Long id);

    @Query("""
        SELECT c FROM Course c
        LEFT JOIN FETCH c.category
        LEFT JOIN FETCH c.teacher
        WHERE c.slug = :slug
    """)
    Optional<Course> findBySlugWithRelations(@Param("slug") String slug);

    // =========================
    // TOP COURSES
    // =========================

    List<Course> findTop10ByStatusTrueOrderByAverageRatingDesc();

    List<Course> findTop10ByStatusTrueOrderByCreatedAtDesc();

    // =========================
    // SEARCH
    // =========================

    @Query("""
        SELECT c FROM Course c
        WHERE c.status = 'PUBLISHED'
        AND LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Course> searchCourses(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // =========================
    // FILTER USER SIDE
    // =========================

    @Query("""
        SELECT c FROM Course c
        LEFT JOIN c.category cat
        WHERE c.status = 'PUBLISHED'
        AND (:categoryId IS NULL OR cat.id = :categoryId)
        AND (:minPrice IS NULL OR c.price >= :minPrice)
        AND (:maxPrice IS NULL OR c.price <= :maxPrice)
        AND (:rating IS NULL OR c.averageRating >= :rating)
    """)
    Page<Course> filterCourses(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("rating") Double rating,
            Pageable pageable
    );

    // =========================
    // ADMIN FILTER
    // =========================

    @Query("""
        SELECT c FROM Course c
        WHERE (:categoryId IS NULL OR c.category.id = :categoryId)
        AND (:status IS NULL OR c.status = :status)
    """)
    Page<Course> adminFilter(
            @Param("categoryId") Long categoryId,
            @Param("status") String status,
            Pageable pageable
    );
}