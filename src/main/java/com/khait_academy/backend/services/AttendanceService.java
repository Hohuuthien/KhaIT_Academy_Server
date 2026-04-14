package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.AttendanceRequest;
import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.entities.Attendance;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.mapper.AttendanceMapper;
import com.khait_academy.backend.repositories.AttendanceRepository;
import com.khait_academy.backend.repositories.LessonRepository;
import com.khait_academy.backend.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    /**
     * CHECK-IN / UPDATE (UPSERT)
     */
    public AttendanceResponse checkIn(AttendanceRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id = " + request.getUserId()));

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found with id = " + request.getLessonId()));

        Attendance attendance = attendanceRepository
                .findByUser_IdAndLesson_Id(user.getId(), lesson.getId())
                .orElseGet(() -> Attendance.builder()
                        .user(user)
                        .lesson(lesson)
                        .build()
                );

        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());
        attendance.setAttendedAt(LocalDateTime.now());

        Attendance saved = attendanceRepository.save(attendance);

        return AttendanceMapper.toResponse(saved);
    }

    /**
     * GET BY LESSON (LIST)
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<AttendanceResponse> getByLesson(Long lessonId) {
        return AttendanceMapper.toList(
                attendanceRepository.findByLesson_Id(lessonId)
        );
    }

    /**
     * GET BY USER (LIST)
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<AttendanceResponse> getByUser(Long userId) {
        return AttendanceMapper.toList(
                attendanceRepository.findByUser_Id(userId)
        );
    }

    /**
     * GET BY LESSON (PAGINATION) 🔥
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<AttendanceResponse> getByLesson(Long lessonId, Pageable pageable) {
        return attendanceRepository.findByLesson_Id(lessonId, pageable)
                .map(AttendanceMapper::toResponse);
    }

    /**
     * GET BY USER (PAGINATION) 🔥
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<AttendanceResponse> getByUser(Long userId, Pageable pageable) {
        return attendanceRepository.findByUser_Id(userId, pageable)
                .map(AttendanceMapper::toResponse);
    }

    /**
     * DELETE
     */
    public void delete(Long id) {

        if (!attendanceRepository.existsById(id)) {
            throw new RuntimeException("Attendance not found with id = " + id);
        }

        attendanceRepository.deleteById(id);
    }
}