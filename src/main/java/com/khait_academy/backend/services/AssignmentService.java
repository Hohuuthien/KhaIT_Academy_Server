package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.AssignmentRequest;
import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.mapper.AssignmentMapper;
import com.khait_academy.backend.repositories.AssignmentRepository;
import com.khait_academy.backend.repositories.LessonRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;

    /**
     * ✅ CREATE
     */
    public AssignmentResponse create(AssignmentRequest request) {

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .lesson(lesson)
                .dueDate(request.getDueDate())
                .maxScore(request.getMaxScore())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return AssignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    /**
     * ✅ GET ALL (pagination)
     */
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getAll(Pageable pageable) {
        return assignmentRepository.findAll(pageable)
                .map(AssignmentMapper::toResponse);
    }

    /**
     * ✅ GET BY ID
     */
    @Transactional(readOnly = true)
    public AssignmentResponse getById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return AssignmentMapper.toResponse(assignment);
    }

    /**
     * ✅ GET BY LESSON (FIX LAZY)
     */
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getByLesson(Long lessonId, Pageable pageable) {

        // ❗ KHÔNG cần existsById → tránh query thừa

        return assignmentRepository.findByLesson_Id(lessonId, pageable)
                .map(AssignmentMapper::toResponse);
    }

    /**
     * ✅ UPDATE
     */
    public AssignmentResponse update(Long id, AssignmentRequest request) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setLesson(lesson);
        assignment.setDueDate(request.getDueDate());
        assignment.setMaxScore(request.getMaxScore());
        assignment.setUpdatedAt(LocalDateTime.now());

        return AssignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    /**
     * ✅ DELETE
     */
    public void delete(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignmentRepository.delete(assignment);
    }
}