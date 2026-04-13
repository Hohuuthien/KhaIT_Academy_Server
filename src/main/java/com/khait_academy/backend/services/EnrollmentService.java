package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Enrollment;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.EnrollmentMapper;
import com.khait_academy.backend.repositories.CourseRepository;
import com.khait_academy.backend.repositories.EnrollmentRepository;
import com.khait_academy.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    // ================= PRIVATE =================

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private User getUserByEmail(String email) {
        validateEmail(email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email không hợp lệ");
        }
    }

    private void validateIds(Long userId, Long courseId) {
        if (userId == null || courseId == null) {
            throw new BadRequestException("UserId và CourseId không được null");
        }
    }

    // ================= WRITE =================

    @Transactional
    public EnrollmentResponse enroll(Long userId, Long courseId) {

        validateIds(userId, courseId);

        // ✅ check trước (UX tốt hơn)
        if (enrollmentRepository.existsByUser_IdAndCourse_Id(userId, courseId)) {
            throw new BadRequestException("Bạn đã đăng ký khóa học này rồi");
        }

        User user = getUser(userId);
        Course course = getCourse(courseId);

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .completed(false)
                .build(); // 👉 dùng @PrePersist set enrolledAt

        try {
            Enrollment saved = enrollmentRepository.save(enrollment);

            log.info("Enroll success: userId={}, courseId={}", userId, courseId);

            return EnrollmentMapper.toResponse(saved);

        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate enrollment (race condition): userId={}, courseId={}", userId, courseId);
            throw new BadRequestException("Bạn đã đăng ký khóa học này rồi");
        }
    }

    @Transactional
    public EnrollmentResponse enrollByEmail(String email, Long courseId) {
        User user = getUserByEmail(email);
        return enroll(user.getId(), courseId);
    }

    // ================= READ =================

    public List<EnrollmentResponse> getMyCourses(Long userId) {

        if (userId == null) {
            throw new BadRequestException("UserId không hợp lệ");
        }

        log.debug("Fetching courses for userId={}", userId);

        return enrollmentRepository.findByUser_IdOrderByEnrolledAtDesc(userId)
                .stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }

    public List<EnrollmentResponse> getMyCoursesByEmail(String email) {
        User user = getUserByEmail(email);
        return getMyCourses(user.getId());
    }

    public boolean isEnrolled(Long userId, Long courseId) {

        if (userId == null || courseId == null) return false;

        return enrollmentRepository.existsByUser_IdAndCourse_Id(userId, courseId);
    }

    public boolean isEnrolledByEmail(String email, Long courseId) {
        User user = getUserByEmail(email);
        return isEnrolled(user.getId(), courseId);
    }

    // ================= ADMIN =================

    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAllByOrderByEnrolledAtDesc()
                .stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }
}