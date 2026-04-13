package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.AttendanceRequest;
import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.mapper.AttendanceMapper;
import com.khait_academy.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    /**
     *  CHECK-IN / UPDATE
     */
    public AttendanceResponse checkIn(AttendanceRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Attendance attendance = attendanceRepository
                .findByUserIdAndLessonId(user.getId(), lesson.getId())
                .orElse(Attendance.builder()
                        .user(user)
                        .lesson(lesson)
                        .build()
                );

        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());
        attendance.setAttendedAt(LocalDateTime.now());

        attendanceRepository.save(attendance);

        return AttendanceMapper.toResponse(attendance);
    }

    /**
     * GET BY LESSON
     */
    public List<AttendanceResponse> getByLesson(Long lessonId) {
        return attendanceRepository.findByLessonId(lessonId)
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    /**
     * GET BY USER
     */
    public List<AttendanceResponse> getByUser(Long userId) {
        return attendanceRepository.findByUserId(userId)
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    /**
     * DELETE
     */
    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }
}