package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.SubmissionRequest;
import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Student;
import com.khait_academy.backend.entities.Submission;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.SubmissionStatus;
import com.khait_academy.backend.mapper.SubmissionMapper;
import com.khait_academy.backend.repositories.AssignmentRepository;
import com.khait_academy.backend.repositories.StudentRepository;
import com.khait_academy.backend.repositories.SubmissionRepository;
import com.khait_academy.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;

    public SubmissionResponse submit(SubmissionRequest request, Authentication authentication) {

        String email = authentication.getName();

        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Submission submission = submissionRepository
                .findByStudent_IdAndAssignment_Id(student.getId(), assignment.getId())
                .orElseGet(() -> Submission.builder()
                        .student(student)
                        .assignment(assignment)
                        .build()
                );

        submission.setFileUrl(request.getFileUrl());
        submission.setSubmittedAt(LocalDateTime.now());

        if (assignment.getDueDate() != null &&
                LocalDateTime.now().isAfter(assignment.getDueDate())) {
            submission.setStatus(SubmissionStatus.LATE);
        } else {
            submission.setStatus(SubmissionStatus.SUBMITTED);
        }

        return SubmissionMapper.toResponse(submissionRepository.save(submission));
    }

    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getMySubmissions(Authentication authentication, Pageable pageable) {

        String email = authentication.getName();

        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return submissionRepository
                .findByStudent_Id(student.getId(), pageable)
                .map(SubmissionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getByStudent(Long studentId, Pageable pageable) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        return submissionRepository
                .findByStudent_Id(studentId, pageable)
                .map(SubmissionMapper::toResponse);
    }
}