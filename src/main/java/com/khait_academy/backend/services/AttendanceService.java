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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    // ================= CREATE =================
    public AttendanceResponse create(AttendanceRequest request) {

        if (attendanceRepository.findByStudentIdAndLessonId(
                request.getStudentId(),
                request.getLessonId()
        ).isPresent()) {
            throw new BadRequestException("Attendance already exists for this student & lesson");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Attendance attendance = AttendanceMapper.toEntity(request, student, lesson);

        return AttendanceMapper.toResponse(
                attendanceRepository.save(attendance)
        );
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAll() {
        return attendanceRepository.findAll()
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public AttendanceResponse getById(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        return AttendanceMapper.toResponse(attendance);
    }

    // ================= UPDATE =================
    public AttendanceResponse update(Long id, AttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        AttendanceMapper.updateEntity(attendance, request);

        return AttendanceMapper.toResponse(
                attendanceRepository.save(attendance)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }

    // ================= BY STUDENT =================
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByStudent(Long studentId) {

        return attendanceRepository.findByStudentId(studentId)
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    // ================= BY LESSON =================
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByLesson(Long lessonId) {

        return attendanceRepository.findByLessonId(lessonId)
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }
}