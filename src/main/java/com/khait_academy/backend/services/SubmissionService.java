package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.SubmissionRequest;
import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.entities.Submission;
import com.khait_academy.backend.enums.SubmissionStatus;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.SubmissionMapper;
import com.khait_academy.backend.repositories.AssignmentRepository;
import com.khait_academy.backend.repositories.StudentRepository;
import com.khait_academy.backend.repositories.SubmissionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;

    // ================= SUBMIT =================
    public SubmissionResponse submit(SubmissionRequest request, Authentication auth) {

        Student student = getStudentByEmail(auth.getName());
        Assignment assignment = getAssignmentOrThrow(request.getAssignmentId());

        Submission submission = getOrCreateSubmission(student, assignment);

        updateSubmission(submission, request, assignment);

        return SubmissionMapper.toResponse(submissionRepository.save(submission));
    }

    // ================= MY SUBMISSIONS =================
    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getMySubmissions(Authentication auth, Pageable pageable) {

        Student student = getStudentByEmail(auth.getName());

        return submissionRepository
                .findByStudent_Id(student.getId(), pageable)
                .map(SubmissionMapper::toResponse);
    }

    // ================= BY STUDENT =================
    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getByStudent(Long studentId, Pageable pageable) {

        validateStudentExists(studentId);

        return submissionRepository
                .findByStudent_Id(studentId, pageable)
                .map(SubmissionMapper::toResponse);
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public SubmissionResponse getById(Long id) {

        Submission submission = getSubmissionOrThrow(id);

        return SubmissionMapper.toResponse(submission);
    }

    // ================= GRADE =================
    public SubmissionResponse grade(Long submissionId, BigDecimal score, String feedback) {

        Submission submission = getSubmissionOrThrow(submissionId);

        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setGradedAt(LocalDateTime.now());
        submission.setStatus(SubmissionStatus.GRADED);

        return SubmissionMapper.toResponse(submissionRepository.save(submission));
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Submission submission = getSubmissionOrThrow(id);

        submissionRepository.delete(submission);
    }

    // ================= HELPERS =================

    private Student getStudentByEmail(String email) {
        return studentRepository.findByUser_Email(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );
    }

    private Assignment getAssignmentOrThrow(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Assignment not found")
                );
    }

    private Submission getSubmissionOrThrow(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Submission not found")
                );
    }

    private Submission getOrCreateSubmission(Student student, Assignment assignment) {
        return submissionRepository
                .findByStudent_IdAndAssignment_Id(student.getId(), assignment.getId())
                .orElseGet(() -> Submission.builder()
                        .student(student)
                        .assignment(assignment)
                        .build()
                );
    }

    private void updateSubmission(Submission submission,
                                  SubmissionRequest request,
                                  Assignment assignment) {

        submission.setFileUrl(request.getFileUrl());
        submission.setSubmittedAt(LocalDateTime.now());

        submission.setStatus(isLate(assignment)
                ? SubmissionStatus.LATE
                : SubmissionStatus.SUBMITTED
        );
    }

    private boolean isLate(Assignment assignment) {
        return assignment.getDueDate() != null
                && LocalDateTime.now().isAfter(assignment.getDueDate());
    }

    private void validateStudentExists(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found");
        }
    }

    @Transactional(readOnly = true)
        public Page<SubmissionResponse> getByAssignment(Long assignmentId, Pageable pageable) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Assignment not found")
                );

        return submissionRepository
                .findByAssignment_Id(assignment.getId(), pageable)
                .map(SubmissionMapper::toResponse);
        }
}