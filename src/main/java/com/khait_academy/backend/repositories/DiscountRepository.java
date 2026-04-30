package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Discount;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @EntityGraph(attributePaths = {"course"})
    List<Discount> findByCourse_Id(Long courseId);

    @Query("""
        SELECT d FROM Discount d
        WHERE d.course.id = :courseId
        AND d.isActive = true
        AND :now BETWEEN d.startDate AND d.endDate
    """)
    List<Discount> findActiveDiscounts(
            @Param("courseId") Long courseId,
            @Param("now") LocalDateTime now
    );

    @Query("""
        SELECT d FROM Discount d
        WHERE d.course.id = :courseId
        AND d.isActive = true
        AND :now BETWEEN d.startDate AND d.endDate
    """)
    List<Discount> findValidDiscounts(
            @Param("courseId") Long courseId,
            @Param("now") LocalDateTime now
    );

    // 🔥 FIX: dùng service chọn best, repo chỉ trả list
    @Query("""
        SELECT d FROM Discount d
        WHERE d.course.id = :courseId
        AND d.isActive = true
        AND :now BETWEEN d.startDate AND d.endDate
    """)
    List<Discount> findValidDiscountsRaw(
            @Param("courseId") Long courseId,
            @Param("now") LocalDateTime now
    );

    @Query("""
        SELECT d FROM Discount d
        WHERE d.course.id IN :courseIds
        AND d.isActive = true
        AND :now BETWEEN d.startDate AND d.endDate
    """)
    List<Discount> findValidDiscountsByCourseIds(
            @Param("courseIds") List<Long> courseIds,
            @Param("now") LocalDateTime now
    );

    @Query("""
        SELECT COUNT(d) > 0 FROM Discount d
        WHERE d.course.id = :courseId
        AND d.isActive = true
        AND (:start <= d.endDate AND :end >= d.startDate)
    """)
    boolean existsOverlappingDiscount(
            @Param("courseId") Long courseId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT COUNT(d) > 0 FROM Discount d
        WHERE d.course.id = :courseId
        AND d.id <> :id
        AND d.isActive = true
        AND (:start <= d.endDate AND :end >= d.startDate)
    """)
    boolean existsOverlappingDiscountExcludeSelf(
            @Param("id") Long id,
            @Param("courseId") Long courseId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}