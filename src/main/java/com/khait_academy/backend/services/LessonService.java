package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.LessonRequest;
import com.khait_academy.backend.dto.response.LessonResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.LessonMapper;
import com.khait_academy.backend.repositories.CourseRepository;
import com.khait_academy.backend.repositories.LessonRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    /**
     * ✅ CREATE
     */
    public LessonResponse create(LessonRequest request) {

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", request.getCourseId())
                );

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .videoUrl(request.getVideoUrl())
                .orderIndex(request.getOrderIndex())
                .course(course)
                .build();

        return LessonMapper.toResponse(
                lessonRepository.save(lesson)
        );
    }

    /**
     * ✅ GET BY COURSE
     */
    public List<LessonResponse> getByCourse(Long courseId) {

        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId)
                .stream()
                .map(LessonMapper::toResponse)
                .toList();
    }

    /**
     * ✅ UPDATE
     */
    public LessonResponse update(Long id, LessonRequest request) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lesson", "id", id)
                );

        lesson.setTitle(request.getTitle());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setOrderIndex(request.getOrderIndex());

        return LessonMapper.toResponse(
                lessonRepository.save(lesson)
        );
    }

    /**
     * ✅ DELETE
     */
    public void delete(Long id) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lesson", "id", id)
                );

        lessonRepository.delete(lesson);
    }
}