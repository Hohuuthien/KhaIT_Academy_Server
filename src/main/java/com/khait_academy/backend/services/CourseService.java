package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.CourseRequest;
import com.khait_academy.backend.dto.response.CourseResponse;
import com.khait_academy.backend.entities.Course;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.CourseMapper;
import com.khait_academy.backend.repositories.CourseRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * ✅ CREATE
     */
    public CourseResponse create(CourseRequest request) {

        Course course = CourseMapper.toEntity(request);

        return CourseMapper.toResponse(
                courseRepository.save(course)
        );
    }

    /**
     * ✅ GET ALL
     */
    public List<CourseResponse> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toResponse)
                .toList();
    }

    /**
     * ✅ GET BY ID
     */
    public CourseResponse getById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id)
                );

        return CourseMapper.toResponse(course);
    }

    /**
     * ✅ UPDATE
     */
    public CourseResponse update(Long id, CourseRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id)
                );

        CourseMapper.updateEntity(course, request);

        return CourseMapper.toResponse(
                courseRepository.save(course)
        );
    }

    /**
     * ✅ DELETE
     */
    public void delete(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id)
                );

        courseRepository.delete(course);
    }
}