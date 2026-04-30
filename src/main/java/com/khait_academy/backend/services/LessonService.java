package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.LessonRequest;
import com.khait_academy.backend.dto.response.LessonResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.LessonMapper;
import com.khait_academy.backend.repositories.CourseRepository;
import com.khait_academy.backend.repositories.LessonRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    // ================= CREATE =================
    public LessonResponse create(LessonRequest request) {

        if (request.getSlug() != null && lessonRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Slug already exists");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = LessonMapper.toEntity(request, course);

        return LessonMapper.toResponse(lessonRepository.save(lesson));
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public Page<LessonResponse> getAll(Pageable pageable) {

        return lessonRepository.findAll(pageable)
                .map(LessonMapper::toResponse);
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public LessonResponse getById(Long id) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        return LessonMapper.toResponse(lesson);
    }

    // ================= UPDATE =================
    public LessonResponse update(Long id, LessonRequest request) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (request.getSlug() != null
                && !request.getSlug().equals(lesson.getSlug())
                && lessonRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Slug already exists");
        }

        LessonMapper.updateEntity(lesson, request);

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
            lesson.setCourse(course);
        }

        return LessonMapper.toResponse(lessonRepository.save(lesson));
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lessonRepository.delete(lesson);
    }

    // ================= GET BY COURSE =================
    @Transactional(readOnly = true)
    public java.util.List<LessonResponse> getByCourse(Long courseId) {

        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId)
                .stream()
                .map(LessonMapper::toResponse)
                .toList();
    }
}