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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;

    // ================= CREATE =================
    public AssignmentResponse create(AssignmentRequest request) {

        Lesson lesson = getLesson(request.getLessonId());

        Assignment assignment = AssignmentMapper.toEntity(request, lesson);

        return AssignmentMapper.toResponse(
                assignmentRepository.save(assignment)
        );
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getAll(Pageable pageable) {

        return assignmentRepository.findAll(pageable)
                .map(AssignmentMapper::toResponse);
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public AssignmentResponse getById(Long id) {
        return AssignmentMapper.toResponse(getAssignment(id));
    }

    // ================= UPDATE =================
    public AssignmentResponse update(Long id, AssignmentRequest request) {

        Assignment assignment = getAssignment(id);

        // update fields
        AssignmentMapper.updateEntity(assignment, request);

        // update lesson nếu khác
        if (request.getLessonId() != null &&
                !request.getLessonId().equals(assignment.getLesson().getId())) {

            assignment.setLesson(getLesson(request.getLessonId()));
        }

        return AssignmentMapper.toResponse(
                assignmentRepository.save(assignment)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {
        assignmentRepository.delete(getAssignment(id));
    }

    // ================= BY LESSON =================
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getByLesson(Long lessonId, Pageable pageable) {

        if (!lessonRepository.existsById(lessonId)) {
            throw new ResourceNotFoundException("Lesson not found");
        }

        return assignmentRepository.findByLessonId(lessonId, pageable)
                .map(AssignmentMapper::toResponse);
    }

    // ================= PUBLISHED =================
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getPublished(Pageable pageable) {

        return assignmentRepository.findByIsPublishedTrue(pageable)
                .map(AssignmentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getPublishedByLesson(Long lessonId, Pageable pageable) {

        if (!lessonRepository.existsById(lessonId)) {
            throw new ResourceNotFoundException("Lesson not found");
        }

        return assignmentRepository
                .findByLessonIdAndIsPublishedTrue(lessonId, pageable)
                .map(AssignmentMapper::toResponse);
    }

    // ================= HELPERS =================
    private Assignment getAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
    }

    private Lesson getLesson(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
    }
}