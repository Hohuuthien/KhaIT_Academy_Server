package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.AssignmentRequest;
import com.khait_academy.backend.dto.response.AssignmentResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.AssignmentMapper;
import com.khait_academy.backend.repositories.AssignmentRepository;
import com.khait_academy.backend.repositories.LessonRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;

    // ================= CREATE =================
    public AssignmentResponse create(AssignmentRequest request) {

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Assignment assignment = AssignmentMapper.toEntity(request, lesson);

        return AssignmentMapper.toResponse(
                assignmentRepository.save(assignment)
        );
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAll() {
        return assignmentRepository.findAll()
                .stream()
                .map(AssignmentMapper::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public AssignmentResponse getById(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        return AssignmentMapper.toResponse(assignment);
    }

    // ================= UPDATE =================
    public AssignmentResponse update(Long id, AssignmentRequest request) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        AssignmentMapper.updateEntity(assignment, request);

        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
            assignment.setLesson(lesson);
        }

        return AssignmentMapper.toResponse(
                assignmentRepository.save(assignment)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        assignmentRepository.delete(assignment);
    }

    // ================= BY LESSON =================
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getByLesson(Long lessonId) {

        return assignmentRepository.findByLessonId(lessonId)
                .stream()
                .map(AssignmentMapper::toResponse)
                .toList();
    }
}