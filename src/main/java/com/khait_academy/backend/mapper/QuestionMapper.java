package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.QuestionOptionRequest;
import com.khait_academy.backend.dto.request.QuestionRequest;
import com.khait_academy.backend.dto.response.QuestionOptionResponse;
import com.khait_academy.backend.dto.response.QuestionResponse;
import com.khait_academy.backend.entities.Question;
import com.khait_academy.backend.entities.QuestionOption;
import com.khait_academy.backend.entities.Quiz;
import com.khait_academy.backend.enums.QuestionType;

import java.util.List;

public final class QuestionMapper {

    private QuestionMapper() {
    }

    // ================= TO ENTITY =================

    public static Question toEntity(
            QuestionRequest request,
            Quiz quiz
    ) {
        Question question = Question.builder()
                .quiz(quiz)
                .content(request.getContent())
                .type(resolveType(request))
                .score(resolveScore(request))
                .build();

        mapOptions(question, request.getOptions());

        return question;
    }

    // ================= TO RESPONSE =================

    public static QuestionResponse toResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .quizId(question.getQuiz().getId())
                .content(question.getContent())
                .type(question.getType())
                .score(question.getScore())
                .options(
                        question.getOptions() == null
                                ? List.of()
                                : question.getOptions()
                                .stream()
                                .map(QuestionMapper::toOptionResponse)
                                .toList()
                )
                .build();
    }

    // ================= PRIVATE HELPERS =================

    private static QuestionType resolveType(
            QuestionRequest request
    ) {
        return request.getType() != null
                ? request.getType()
                : QuestionType.MULTIPLE_CHOICE;
    }

    private static Integer resolveScore(
            QuestionRequest request
    ) {
        return request.getScore() != null
                ? request.getScore()
                : 1;
    }

    private static void mapOptions(
            Question question,
            List<QuestionOptionRequest> options
    ) {
        if (options == null || options.isEmpty()) {
            return;
        }

        options.stream()
                .map(QuestionMapper::toOptionEntity)
                .forEach(question::addOption);
    }

    private static QuestionOption toOptionEntity(
            QuestionOptionRequest request
    ) {
        return QuestionOption.builder()
                .content(request.getContent())
                .correct(request.isCorrect())
                .build();
    }

    private static QuestionOptionResponse toOptionResponse(
            QuestionOption option
    ) {
        return QuestionOptionResponse.builder()
                .id(option.getId())
                .content(option.getContent())
                .correct(option.isCorrect())
                .build();
    }
}