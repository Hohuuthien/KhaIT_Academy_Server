package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.AttendanceRequest;
import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.entities.Attendance;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.AttendanceMapper;
import com.khait_academy.backend.repositories.AttendanceRepository;
import com.khait_academy.backend.repositories.LessonRepository;
import com.khait_academy.backend.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    // ================= CHECK-IN =================
    public AttendanceResponse checkIn(AttendanceRequest request) {

        validateNotExists(request.getStudentId(), request.getLessonId());

        Student student = getStudentOrThrow(request.getStudentId());
        Lesson lesson = getLessonOrThrow(request.getLessonId());

        Attendance attendance = AttendanceMapper.toEntity(request, student, lesson);

        return AttendanceMapper.toResponse(
                attendanceRepository.save(attendance)
        );
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public AttendanceResponse getById(Long id) {
        return AttendanceMapper.toResponse(getAttendanceOrThrow(id));
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getAll(Pageable pageable) {

        return attendanceRepository.findAll(pageable)
                .map(AttendanceMapper::toResponse);
    }

    // ================= GET BY STUDENT =================
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getByStudent(Long studentId, Pageable pageable) {

        validateStudentExists(studentId);

        return attendanceRepository
                .findByStudentId(studentId, pageable)
                .map(AttendanceMapper::toResponse);
    }

    // ================= GET BY LESSON =================
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getByLesson(Long lessonId, Pageable pageable) {

        validateLessonExists(lessonId);

        return attendanceRepository
                .findByLessonId(lessonId, pageable)
                .map(AttendanceMapper::toResponse);
    }

    // ================= DELETE =================
    public void delete(Long id) {
        attendanceRepository.delete(getAttendanceOrThrow(id));
    }

    // ================= HELPERS =================

    private Attendance getAttendanceOrThrow(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attendance not found")
                );
    }

    private Student getStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );
    }

    private Lesson getLessonOrThrow(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lesson not found")
                );
    }

    private void validateNotExists(Long studentId, Long lessonId) {
        if (attendanceRepository
                .findByStudentIdAndLessonId(studentId, lessonId)
                .isPresent()) {

            throw new BadRequestException(
                    "Attendance already exists for this student & lesson"
            );
        }
    }

    private void validateStudentExists(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found");
        }
    }

    private void validateLessonExists(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new ResourceNotFoundException("Lesson not found");
        }
    }
}