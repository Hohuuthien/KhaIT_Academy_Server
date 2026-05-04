package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.ReviewRequest;
import com.khait_academy.backend.dto.response.ReviewResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.enums.CourseStatus;
import com.khait_academy.backend.enums.EnrollmentStatus;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.ReviewMapper;
import com.khait_academy.backend.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    // ================= CREATE =================

    public ReviewResponse create(ReviewRequest request, Long studentId) {

        validateDuplicateReview(studentId, request.getCourseId());

        Student student = findStudent(studentId);
        Course course = findCourse(request.getCourseId());

        validateCourseIsPublished(course);
        validateStudentEnrollment(studentId, course.getId());

        Review review = Review.builder()
                .student(student)
                .course(course)
                .rating(request.getRating())
                .comment(request.getComment())
                .isApproved(false)
                .build();

        Review saved = reviewRepository.save(review);

        return ReviewMapper.toResponse(saved, studentId);
    }

    // ================= GET =================

    @Transactional(readOnly = true)
    public List<ReviewResponse> getByCourse(Long courseId, Long currentStudentId) {

        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        return reviewRepository.findByCourse_IdAndIsApprovedTrue(courseId)
                .stream()
                .map(r -> ReviewMapper.toResponse(r, currentStudentId))
                .toList();
    }

    // ================= APPROVE =================

    public void approve(Long reviewId) {

        Review review = findReview(reviewId);

        if (Boolean.TRUE.equals(review.getIsApproved())) {
            return;
        }

        review.setIsApproved(true);
        reviewRepository.save(review);

        recalculateCourseRating(review.getCourse().getId());
    }

    // ================= DELETE =================

    public void delete(Long reviewId, Long studentId) {

        Review review = reviewRepository.findByIdAndStudent_Id(reviewId, studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "id", reviewId)
                );

        Long courseId = review.getCourse().getId();

        reviewRepository.delete(review);

        recalculateCourseRating(courseId);
    }

    // ================= PRIVATE =================

    private void validateDuplicateReview(Long studentId, Long courseId) {
        if (reviewRepository.existsByStudent_IdAndCourse_Id(studentId, courseId)) {
            throw new BadRequestException("Bạn đã đánh giá khóa học này rồi");
        }
    }

    private void validateCourseIsPublished(Course course) {
        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new BadRequestException("Khóa học chưa được publish");
        }
    }

    private void validateStudentEnrollment(Long studentId, Long courseId) {

        boolean enrolled = enrollmentRepository
                .findByStudent_IdAndCourse_Id(studentId, courseId)
                .map(e -> e.getStatus() == EnrollmentStatus.ACTIVE
                        || e.getStatus() == EnrollmentStatus.COMPLETED)
                .orElse(false);

        if (!enrolled) {
            throw new BadRequestException("Bạn phải đăng ký khóa học trước");
        }
    }

    private void recalculateCourseRating(Long courseId) {

        Double avg = reviewRepository.getAverageRating(courseId);
        Long total = reviewRepository.countApproved(courseId);

        Course course = findCourse(courseId);

        course.setAverageRating(
                avg != null ? BigDecimal.valueOf(avg) : BigDecimal.ZERO
        );

        course.setTotalReviews(
                total != null ? total.intValue() : 0
        );

        courseRepository.save(course);
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student", "id", id)
                );
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id)
                );
    }

    private Review findReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "id", id)
                );
    }
}