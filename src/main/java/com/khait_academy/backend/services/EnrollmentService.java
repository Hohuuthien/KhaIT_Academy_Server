package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.EnrollmentRequest;
import com.khait_academy.backend.dto.response.EnrollmentResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Enrollment;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.EnrollmentMapper;
import com.khait_academy.backend.repositories.CourseRepository;
import com.khait_academy.backend.repositories.EnrollmentRepository;
import com.khait_academy.backend.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // ================= CREATE =================
    public EnrollmentResponse create(EnrollmentRequest request) {

        validateNotEnrolled(request.getStudentId(), request.getCourseId());

        Student student = getStudent(request.getStudentId());
        Course course = getCourse(request.getCourseId());

        Enrollment enrollment = EnrollmentMapper.toEntity(request, student, course);

        enrollment.setPriceAtPurchase(resolveCoursePrice(course));

        return toResponse(enrollmentRepository.save(enrollment));
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public EnrollmentResponse getById(Long id) {
        return toResponse(getEnrollment(id));
    }

    // ================= UPDATE =================
    public EnrollmentResponse update(Long id, EnrollmentRequest request) {

        Enrollment enrollment = getEnrollment(id);

        EnrollmentMapper.updateEntity(enrollment, request);

        // ❗ KHÔNG cho update price (giữ lịch sử)
        return toResponse(enrollmentRepository.save(enrollment));
    }

    // ================= DELETE =================
    public void delete(Long id) {
        enrollmentRepository.delete(getEnrollment(id));
    }

    // ================= BY STUDENT =================
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByStudent(Long studentId) {
        return enrollmentRepository.findByStudent_Id(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================= BY COURSE =================
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByCourse(Long courseId) {
        return enrollmentRepository.findByCourse_Id(courseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================= PRIVATE METHODS =================

    private void validateNotEnrolled(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudent_IdAndCourse_Id(studentId, courseId)) {
            throw new BadRequestException("Student already enrolled in this course");
        }
    }

    private Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private Enrollment getEnrollment(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
    }

    private BigDecimal resolveCoursePrice(Course course) {
        if (course.getPrice() == null) {
            throw new BadRequestException("Course price is missing");
        }
        return course.getPrice();
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return EnrollmentMapper.toResponse(enrollment);
    }
}