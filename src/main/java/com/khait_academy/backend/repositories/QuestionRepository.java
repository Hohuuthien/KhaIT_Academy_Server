package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Question;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // ================= BASIC =================

    @EntityGraph(attributePaths = {"options"})
    List<Question> findByQuiz_Id(Long quizId);

    // ================= ORDERED =================

    @Query("""
        SELECT q FROM Question q
        LEFT JOIN FETCH q.options
        WHERE q.quiz.id = :quizId
        ORDER BY q.id ASC
    """)
    List<Question> findByQuizIdWithOptions(@Param("quizId") Long quizId);

    // ================= QUIZ LOAD =================

    @Query("""
        SELECT DISTINCT q FROM Question q
        LEFT JOIN FETCH q.options
        WHERE q.quiz.id = :quizId
    """)
    List<Question> findQuizQuestions(@Param("quizId") Long quizId);

    // ================= SINGLE =================

    @EntityGraph(attributePaths = {"options"})
    Optional<Question> findWithOptionsById(Long id);

    // ================= ADMIN =================

    long countByQuiz_Id(Long quizId);

    boolean existsByIdAndQuiz_Id(Long id, Long quizId);
}