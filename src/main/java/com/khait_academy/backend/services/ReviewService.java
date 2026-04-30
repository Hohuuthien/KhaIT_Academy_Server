package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.ReviewRequest;
import com.khait_academy.backend.dto.response.ReviewResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.enums.EnrollmentStatus;
import com.khait_academy.backend.enums.CourseStatus;
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

    // ================= CREATE REVIEW =================
    public ReviewResponse create(ReviewRequest request, Long studentId) {

        // check duplicate
        if (reviewRepository.existsByStudent_IdAndCourse_Id(studentId, request.getCourseId())) {
            throw new BadRequestException("Bạn đã đánh giá khóa học này rồi");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student", "id", studentId)
                );

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", request.getCourseId())
                );

        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new BadRequestException("Khóa học chưa được publish");
        }

        boolean isEnrolled = enrollmentRepository
                .findByStudent_IdAndCourse_Id(studentId, request.getCourseId())
                .map(e ->
                        e.getStatus() == EnrollmentStatus.ACTIVE ||
                        e.getStatus() == EnrollmentStatus.COMPLETED
                )
                .orElse(false);

        if (!isEnrolled) {
            throw new BadRequestException("Bạn phải đăng ký khóa học trước");
        }

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

    // ================= GET BY COURSE =================
    @Transactional(readOnly = true)
    public List<ReviewResponse> getByCourse(Long courseId, Long currentStudentId) {

        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        return reviewRepository
                .findByCourse_IdAndIsApprovedTrue(courseId)
                .stream()
                .map(r -> ReviewMapper.toResponse(r, currentStudentId))
                .toList();
    }

    // ================= APPROVE REVIEW =================
    public void approve(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "id", reviewId)
                );

        if (Boolean.TRUE.equals(review.getIsApproved())) {
            return;
        }

        review.setIsApproved(true);
        reviewRepository.save(review);

        updateCourseRating(review.getCourse().getId());
    }

    // ================= DELETE REVIEW =================
    public void delete(Long reviewId, Long studentId) {

        Review review = reviewRepository.findByIdAndStudent_Id(reviewId, studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "id", reviewId)
                );

        Long courseId = review.getCourse().getId();

        reviewRepository.delete(review);

        updateCourseRating(courseId);
    }

    // ================= UPDATE RATING =================
    private void updateCourseRating(Long courseId) {

        Double avg = reviewRepository.getAverageRating(courseId);
        Long total = reviewRepository.countApproved(courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", courseId)
                );

        course.setAverageRating(
                avg != null ? BigDecimal.valueOf(avg) : BigDecimal.ZERO
        );

        course.setTotalReviews(
                total != null ? total.intValue() : 0
        );

        courseRepository.save(course);
    }
}