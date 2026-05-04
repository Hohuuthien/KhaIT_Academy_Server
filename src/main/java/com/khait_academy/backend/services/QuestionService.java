package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.QuestionRequest;
import com.khait_academy.backend.dto.response.QuestionResponse;
import com.khait_academy.backend.entities.Question;
import com.khait_academy.backend.entities.QuestionOption;
import com.khait_academy.backend.entities.Quiz;
import com.khait_academy.backend.enums.QuestionType;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.QuestionMapper;
import com.khait_academy.backend.repositories.QuestionRepository;
import com.khait_academy.backend.repositories.QuizRepository;

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

    public QuestionResponse create(QuestionRequest request) {

        validateRequest(request);

        Quiz quiz = findQuizById(request.getQuizId());

        Question question = QuestionMapper.toEntity(request, quiz);

        Question saved = questionRepository.save(question);

        return QuestionMapper.toResponse(saved);
    }

    // ================= GET BY QUIZ =================

    @Transactional(readOnly = true)
    public List<QuestionResponse> getByQuiz(Long quizId) {

        findQuizById(quizId);

        return questionRepository.findByQuiz_Id(quizId)
                .stream()
                .map(QuestionMapper::toResponse)
                .toList();
    }
    // =================UPDATE================
    public QuestionResponse update(
        Long id,
        QuestionRequest request
    ) {
        validateRequest(request);

        Question question = findQuestionById(id);
        Quiz quiz = findQuizById(request.getQuizId());

        question.setQuiz(quiz);
        question.setContent(request.getContent());
        question.setType(request.getType());
        question.setScore(request.getScore());

        question.replaceOptions(
                request.getOptions() == null
                        ? List.of()
                        : request.getOptions()
                        .stream()
                        .map(option -> QuestionOption.builder()
                                .content(option.getContent())
                                .correct(option.isCorrect())
                                .build())
                        .toList()
        );

        Question updated = questionRepository.save(question);

        return QuestionMapper.toResponse(updated);
    }

    // ================= DELETE =================

    public void delete(Long id) {

        Question question = findQuestionById(id);

        questionRepository.delete(question);
    }

    // ================= PRIVATE =================

    private Quiz findQuizById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Quiz not found"));
    }

    private Question findQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Question not found"));
    }

    private void validateRequest(QuestionRequest request) {

        validateBasic(request);

        if (request.getType() == QuestionType.MULTIPLE_CHOICE) {
            validateMultipleChoice(request);
        }
    }

    private void validateBasic(QuestionRequest request) {

        if (request.getQuizId() == null) {
            throw new BadRequestException("Quiz ID is required");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new BadRequestException("Question content is required");
        }

        if (request.getScore() != null && request.getScore() <= 0) {
            throw new BadRequestException("Score must be greater than 0");
        }
    }

    private void validateMultipleChoice(
            QuestionRequest request
    ) {
        if (request.getOptions() == null || request.getOptions().isEmpty()) {
            throw new BadRequestException(
                    "Multiple choice question must have options"
            );
        }

        boolean hasCorrectAnswer = request.getOptions()
                .stream()
                .anyMatch(option -> option.isCorrect());

        if (!hasCorrectAnswer) {
            throw new BadRequestException(
                    "At least one correct answer is required"
            );
        }
    }
}