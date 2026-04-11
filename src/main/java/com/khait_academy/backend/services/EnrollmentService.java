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

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * ✅ ENROLL COURSE
     */
    @Transactional
    public EnrollmentResponse enroll(Long userId, Long courseId) {

        // Check tồn tại (optional - vẫn cần DB constraint)
        if (enrollmentRepository.existsByUser_IdAndCourse_Id(userId, courseId)) {
            throw new BadRequestException("Bạn đã đăng ký khóa học này rồi");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .enrolledAt(LocalDateTime.now())
                .completed(false)
                .build();

        try {
            return EnrollmentMapper.toResponse(
                    enrollmentRepository.save(enrollment)
            );
        } catch (DataIntegrityViolationException e) {
            // phòng trường hợp race condition
            throw new BadRequestException("Bạn đã đăng ký khóa học này rồi");
        }
    }

    /**
     * ✅ GET MY COURSES
     */
    public List<EnrollmentResponse> getMyCourses(Long userId) {
        return enrollmentRepository.findByUser_IdOrderByEnrolledAtDesc(userId)
                .stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }

    /**
     * ✅ CHECK ENROLLED
     */
    public boolean isEnrolled(Long userId, Long courseId) {
        return enrollmentRepository.existsByUser_IdAndCourse_Id(userId, courseId);
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}