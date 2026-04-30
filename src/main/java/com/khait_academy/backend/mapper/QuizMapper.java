package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.QuizRequest;
import com.khait_academy.backend.dto.response.QuizResponse;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.Quiz;

public class QuizMapper {

    // ================= TO ENTITY =================
    public static Quiz toEntity(QuizRequest req, Lesson lesson) {

        return Quiz.builder()
                .lesson(lesson)
                .title(req.getTitle())
                .description(req.getDescription())
                .timeLimit(req.getTimeLimit())
                .maxAttempts(req.getMaxAttempts())
                .passScore(req.getPassScore())
                .isPublished(req.getIsPublished() != null ? req.getIsPublished() : false)
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .build();
    }

    // ================= TO RESPONSE =================
    public static QuizResponse toResponse(Quiz q) {

        return QuizResponse.builder()
                .id(q.getId())
                .lessonId(q.getLesson() != null ? q.getLesson().getId() : null)
                .title(q.getTitle())
                .description(q.getDescription())
                .timeLimit(q.getTimeLimit())
                .maxAttempts(q.getMaxAttempts())
                .passScore(q.getPassScore())
                .isPublished(q.getIsPublished())
                .startTime(q.getStartTime())
                .endTime(q.getEndTime())
                .createdAt(q.getCreatedAt())
                .updatedAt(q.getUpdatedAt())
                .build();
    }

    // ================= UPDATE =================
    public static void update(Quiz quiz, QuizRequest req, Lesson lesson) {

        if (lesson != null) {
            quiz.setLesson(lesson);
        }

        if (req.getTitle() != null) {
            quiz.setTitle(req.getTitle());
        }

        if (req.getDescription() != null) {
            quiz.setDescription(req.getDescription());
        }

        if (req.getTimeLimit() != null) {
            quiz.setTimeLimit(req.getTimeLimit());
        }

        if (req.getMaxAttempts() != null) {
            quiz.setMaxAttempts(req.getMaxAttempts());
        }

        if (req.getPassScore() != null) {
            quiz.setPassScore(req.getPassScore());
        }

        if (req.getIsPublished() != null) {
            quiz.setIsPublished(req.getIsPublished());
        }

        if (req.getStartTime() != null) {
            quiz.setStartTime(req.getStartTime());
        }

        if (req.getEndTime() != null) {
            quiz.setEndTime(req.getEndTime());
        }
    }
}