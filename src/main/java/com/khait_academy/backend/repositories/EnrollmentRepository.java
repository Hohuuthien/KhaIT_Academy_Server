package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // ================= USER =================

    // 👉 dùng cho list đơn giản (không cần course detail)
    List<Enrollment> findByUser_Id(Long userId);

    // 👉 chuẩn nhất (có course + user + sort)
    @EntityGraph(attributePaths = {"course"})
    List<Enrollment> findByUser_IdOrderByEnrolledAtDesc(Long userId);

    // 👉 pagination + fetch course (QUAN TRỌNG)
    @EntityGraph(attributePaths = {"course"})
    Page<Enrollment> findByUser_Id(Long userId, Pageable pageable);


    // ================= COURSE =================

    @EntityGraph(attributePaths = {"user"})
    List<Enrollment> findByCourse_Id(Long courseId);

    @EntityGraph(attributePaths = {"user"})
    Page<Enrollment> findByCourse_Id(Long courseId, Pageable pageable);


    // ================= CHECK =================

    Optional<Enrollment> findByUser_IdAndCourse_Id(Long userId, Long courseId);

    boolean existsByUser_IdAndCourse_Id(Long userId, Long courseId);


    // ================= ADMIN =================

    // 👉 danh sách full có relation
    @EntityGraph(attributePaths = {"user", "course"})
    List<Enrollment> findAllByOrderByEnrolledAtDesc();

    // 👉 pagination admin
    @EntityGraph(attributePaths = {"user", "course"})
    Page<Enrollment> findAll(Pageable pageable);


    // ================= CUSTOM QUERY (OPTIONAL - PRO) =================

    // 👉 fetch join (tối ưu hơn entity graph trong vài trường hợp)
    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.course
        WHERE e.user.id = :userId
        ORDER BY e.enrolledAt DESC
    """)
    List<Enrollment> findMyCoursesFetch(Long userId);
}