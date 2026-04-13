package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.SubmissionRequest;
import com.khait_academy.backend.dto.response.SubmissionResponse;
import com.khait_academy.backend.entities.Assignment;
import com.khait_academy.backend.entities.Submission;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.SubmissionStatus;
import com.khait_academy.backend.mapper.SubmissionMapper;
import com.khait_academy.backend.repositories.AssignmentRepository;
import com.khait_academy.backend.repositories.SubmissionRepository;
import com.khait_academy.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    /**
     * ✅ SUBMIT (JWT - không dùng userId)
     */
    public SubmissionResponse submit(SubmissionRequest request, Authentication authentication) {

        // lấy email từ JWT
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Submission submission = submissionRepository
                .findByUserIdAndAssignmentId(user.getId(), assignment.getId())
                .orElse(Submission.builder()
                        .user(user)
                        .assignment(assignment)
                        .build()
                );

        submission.setFileUrl(request.getFileUrl());
        submission.setSubmittedAt(LocalDateTime.now());

        // check deadline
        if (assignment.getDueDate() != null &&
                LocalDateTime.now().isAfter(assignment.getDueDate())) {
            submission.setStatus(SubmissionStatus.LATE);
        } else {
            submission.setStatus(SubmissionStatus.SUBMITTED);
        }

        submissionRepository.save(submission);

        return SubmissionMapper.toResponse(submission);
    }

    /**
     * ✅ GET BY ID
     */
    @Transactional(readOnly = true)
    public SubmissionResponse getById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        return SubmissionMapper.toResponse(submission);
    }

    /**
     * ✅ GET BY ASSIGNMENT
     */
    @Transactional(readOnly = true)
    public List<SubmissionResponse> getByAssignment(Long assignmentId) {

        if (!assignmentRepository.existsById(assignmentId)) {
            throw new RuntimeException("Assignment not found");
        }

        return submissionRepository.findByAssignmentId(assignmentId)
                .stream()
                .map(SubmissionMapper::toResponse)
                .toList();
    }

    /**
     * ✅ GET BY USER
     */
    @Transactional(readOnly = true)
    public List<SubmissionResponse> getByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        return submissionRepository.findByUserId(userId)
                .stream()
                .map(SubmissionMapper::toResponse)
                .toList();
    }

    /**
     * ✅ GRADE
     */
    public SubmissionResponse grade(Long id, Double score, String feedback) {

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Double maxScore = submission.getAssignment().getMaxScore();

        if (score < 0 || (maxScore != null && score > maxScore)) {
            throw new RuntimeException("Invalid score");
        }

        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setStatus(SubmissionStatus.GRADED);

        submissionRepository.save(submission);

        return SubmissionMapper.toResponse(submission);
    }

    /**
     * ✅ DELETE
     */
    public void delete(Long id) {

        if (!submissionRepository.existsById(id)) {
            throw new RuntimeException("Submission not found");
        }

        submissionRepository.deleteById(id);
    }
}