package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.LessonProgressRequest;
import com.khait_academy.backend.dto.response.LessonProgressResponse;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.LessonProgress;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.LessonProgressMapper;
import com.khait_academy.backend.repositories.LessonProgressRepository;
import com.khait_academy.backend.repositories.LessonRepository;
import com.khait_academy.backend.repositories.StudentRepository;

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

    /**
     * CREATE OR UPDATE PROGRESS
     */
    public LessonProgressResponse saveOrUpdate(
            Long userId,
            LessonProgressRequest request
    ) {

        Student student = getStudentByUserId(userId);
        Lesson lesson = getLesson(request.getLessonId());

        LessonProgress progress = lessonProgressRepository
                .findByStudentIdAndLessonId(
                        student.getId(),
                        lesson.getId()
                )
                .orElseGet(() -> buildNewProgress(student, lesson));

        updateProgress(progress, request);

        LessonProgress saved = lessonProgressRepository.save(progress);

        return LessonProgressMapper.toResponse(saved);
    }

    /**
     * GET MY PROGRESS
     */
    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getMyProgress(Long userId) {

        Student student = getStudentByUserId(userId);

        return getByStudent(student.getId());
    }

    /**
     * GET PROGRESS BY STUDENT
     */
    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getByStudent(Long studentId) {

        return lessonProgressRepository.findByStudentId(studentId)
                .stream()
                .map(LessonProgressMapper::toResponse)
                .toList();
    }

    /**
     * GET PROGRESS BY LESSON
     */
    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getByLesson(Long lessonId) {

        return lessonProgressRepository.findByLessonId(lessonId)
                .stream()
                .map(LessonProgressMapper::toResponse)
                .toList();
    }

    /**
     * DELETE
     */
    public void delete(Long id) {

        LessonProgress progress = lessonProgressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("LessonProgress not found"));

        lessonProgressRepository.delete(progress);
    }

    // ================= PRIVATE HELPERS =================

    private Student getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));
    }

    private Lesson getLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lesson not found"));
    }

    private LessonProgress buildNewProgress(Student student, Lesson lesson) {
        return LessonProgress.builder()
                .student(student)
                .lesson(lesson)
                .progress(0)
                .lastPosition(0)
                .build();
    }

    private void updateProgress(
            LessonProgress progress,
            LessonProgressRequest request
    ) {

        if (request.getProgress() != null) {
            validateProgress(request.getProgress());
            progress.setProgress(request.getProgress());
        }

        if (request.getLastPosition() != null) {
            validateLastPosition(request.getLastPosition());
            progress.setLastPosition(request.getLastPosition());
        }
    }

    private void validateProgress(Integer progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException(
                    "Progress must be between 0 and 100"
            );
        }
    }

    private void validateLastPosition(Integer lastPosition) {
        if (lastPosition < 0) {
            throw new IllegalArgumentException(
                    "Last position cannot be negative"
            );
        }
    }
}