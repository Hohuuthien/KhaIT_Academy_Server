package com.khait_academy.backend.mapper;


import com.khait_academy.backend.dto.request.QuestionRequest;
import com.khait_academy.backend.dto.response.*;
import com.khait_academy.backend.entities.*;

import java.util.List;

public class QuestionMapper {

    // ================= TO ENTITY =================
    public static Question toEntity(QuestionRequest req, Quiz quiz) {

        Question question = Question.builder()
                .quiz(quiz)
                .content(req.getContent())
                .type(req.getType())
                .score(req.getScore())
                .build();

        List<QuestionOption> options = req.getOptions().stream()
                .map(o -> QuestionOption.builder()
                        .question(question)
                        .content(o.getContent())
                        .isCorrect(o.isCorrect())
                        .build()
                )
                .toList();

        question.setOptions(options);

        return question;
    }

    // ================= TO RESPONSE =================
    public static QuestionResponse toResponse(Question q) {

        return QuestionResponse.builder()
                .id(q.getId())
                .quizId(q.getQuiz().getId()) // 🔥 FIX
                .content(q.getContent())
                .type(q.getType())
                .score(q.getScore())

                .options(
                        q.getOptions() == null
                                ? List.of()
                                : q.getOptions().stream()
                                .map(o -> QuestionOptionResponse.builder()
                                        .id(o.getId())
                                        .content(o.getContent())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}