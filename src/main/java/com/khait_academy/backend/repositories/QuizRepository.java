package com.khait_academy.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.khait_academy.backend.entities.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByLesson_Id(Long lessonId);

    @Query("""
        SELECT q FROM Quiz q
        WHERE q.lesson.id = :lessonId
        AND q.isPublished = true
    """)
    List<Quiz> findPublishedByLesson(Long lessonId);
}
