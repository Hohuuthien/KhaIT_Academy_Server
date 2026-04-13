package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.AssignmentRequest;
import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.mapper.AssignmentMapper;
import com.khait_academy.backend.repositories.AssignmentRepository;
import com.khait_academy.backend.repositories.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
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

        assignmentRepository.save(assignment);

        return AssignmentMapper.toResponse(assignment);
    }

    /**
     * ✅ GET ALL
     */
    public List<AssignmentResponse> getAll() {
        return assignmentRepository.findAll()
                .stream()
                .map(AssignmentMapper::toResponse)
                .toList();
    }

    /**
     * ✅ GET BY ID
     */
    public AssignmentResponse getById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return AssignmentMapper.toResponse(assignment);
    }

    /**
     * ✅ GET BY LESSON
     */
    public List<AssignmentResponse> getByLesson(Long lessonId) {

        // check lesson tồn tại (best practice)
        if (!lessonRepository.existsById(lessonId)) {
            throw new RuntimeException("Lesson not found");
        }

        return assignmentRepository.findByLessonId(lessonId)
                .stream()
                .map(AssignmentMapper::toResponse)
                .toList();
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

        assignmentRepository.save(assignment);

        return AssignmentMapper.toResponse(assignment);
    }

    /**
     * ✅ DELETE
     */
    public void delete(Long id) {

        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found");
        }

        assignmentRepository.deleteById(id);
    }
}