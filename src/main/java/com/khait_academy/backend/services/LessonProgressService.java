package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.LessonProgressRequest;
import com.khait_academy.backend.dto.response.LessonProgressResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.LessonProgressMapper;
import com.khait_academy.backend.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    // ================= CREATE / UPDATE (UPSERT) =================
    public LessonProgressResponse saveOrUpdate(LessonProgressRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        LessonProgress lp = lessonProgressRepository
                .findByStudentIdAndLessonId(request.getStudentId(), request.getLessonId())
                .orElse(LessonProgress.builder()
                        .student(student)
                        .lesson(lesson)
                        .build());

        if (request.getProgress() != null) {
            lp.setProgress(request.getProgress());
        }

        if (request.getLastPosition() != null) {
            lp.setLastPosition(request.getLastPosition());
        }

        return LessonProgressMapper.toResponse(
                lessonProgressRepository.save(lp)
        );
    }

    // ================= GET BY STUDENT =================
    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getByStudent(Long studentId) {

        return lessonProgressRepository.findByStudentId(studentId)
                .stream()
                .map(LessonProgressMapper::toResponse)
                .toList();
    }

    // ================= GET BY LESSON =================
    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getByLesson(Long lessonId) {

        return lessonProgressRepository.findByLessonId(lessonId)
                .stream()
                .map(LessonProgressMapper::toResponse)
                .toList();
    }

    // ================= DELETE =================
    public void delete(Long id) {

        LessonProgress lp = lessonProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LessonProgress not found"));

        lessonProgressRepository.delete(lp);
    }
}