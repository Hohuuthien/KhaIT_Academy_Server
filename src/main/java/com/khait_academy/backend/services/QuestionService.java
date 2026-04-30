package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.QuestionRequest;
import com.khait_academy.backend.dto.response.QuestionResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.mapper.QuestionMapper;
import com.khait_academy.backend.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    // ================= CREATE =================
    public QuestionResponse create(QuestionRequest req) {

        Quiz quiz = quizRepository.findById(req.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Question question = QuestionMapper.toEntity(req, quiz);

        return QuestionMapper.toResponse(
                questionRepository.save(question)
        );
    }

    // ================= GET BY QUIZ =================
    @Transactional(readOnly = true)
    public List<QuestionResponse> getByQuiz(Long quizId) {

        return questionRepository.findByQuiz_Id(quizId)
                .stream()
                .map(QuestionMapper::toResponse)
                .toList();
    }

    // ================= DELETE =================
    public void delete(Long id) {

        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found");
        }

        questionRepository.deleteById(id);
    }
}