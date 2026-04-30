package com.khait_academy.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.khait_academy.backend.dto.request.QuizRequest;
import com.khait_academy.backend.dto.response.QuizResponse;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.Quiz;
import com.khait_academy.backend.mapper.QuizMapper;
import com.khait_academy.backend.repositories.LessonRepository;
import com.khait_academy.backend.repositories.QuizRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {

    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;

    // ================= CREATE =================
    public QuizResponse create(QuizRequest req) {

        Lesson lesson = lessonRepository.findById(req.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Quiz quiz = QuizMapper.toEntity(req, lesson);

        return QuizMapper.toResponse(
                quizRepository.save(quiz)
        );
    }

    // ================= GET BY LESSON =================
    @Transactional(readOnly = true)
    public List<QuizResponse> getByLesson(Long lessonId) {

        return quizRepository.findByLesson_Id(lessonId)
                .stream()
                .map(QuizMapper::toResponse)
                .toList();
    }

    // ================= GET ONE =================
    @Transactional(readOnly = true)
    public QuizResponse getOne(Long id) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return QuizMapper.toResponse(quiz);
    }

    // ================= UPDATE =================
    public QuizResponse update(Long id, QuizRequest req) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Lesson lesson = null;
        if (req.getLessonId() != null) {
            lesson = lessonRepository.findById(req.getLessonId())
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
        }

        QuizMapper.update(quiz, req, lesson);

        return QuizMapper.toResponse(quizRepository.save(quiz));
    }

    // ================= DELETE =================
    public void delete(Long id) {

        if (!quizRepository.existsById(id)) {
            throw new RuntimeException("Quiz not found");
        }

        quizRepository.deleteById(id);
    }
}