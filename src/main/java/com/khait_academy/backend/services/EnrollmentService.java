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

        if (enrollmentRepository.existsByStudent_IdAndCourse_Id(
                request.getStudentId(),
                request.getCourseId()
        )) {
            throw new BadRequestException("Student already enrolled in this course");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found")
                );

        Enrollment enrollment = EnrollmentMapper.toEntity(request, student, course);

        return EnrollmentMapper.toResponse(
                enrollmentRepository.save(enrollment)
        );
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll()
                .stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public EnrollmentResponse getById(Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found")
                );

        return EnrollmentMapper.toResponse(enrollment);
    }

    // ================= UPDATE =================
    public EnrollmentResponse update(Long id, EnrollmentRequest request) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found")
                );

        EnrollmentMapper.updateEntity(enrollment, request);

        return EnrollmentMapper.toResponse(
                enrollmentRepository.save(enrollment)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found")
                );

        enrollmentRepository.delete(enrollment);
    }

    // ================= BY STUDENT =================
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByStudent(Long studentId) {

        return enrollmentRepository.findByStudent_Id(studentId)
                .stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }

    // ================= BY COURSE =================
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByCourse(Long courseId) {

        return enrollmentRepository.findByCourse_Id(courseId)
                .stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }
}