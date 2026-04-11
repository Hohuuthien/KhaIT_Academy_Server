package com.khait_academy.backend.repositories;

import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.LessonProgress;
import com.khait_academy.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    // tìm theo user + lesson
    Optional<LessonProgress> findByUserAndLesson(User user, Lesson lesson);

    // 🔥 QUAN TRỌNG: đúng chuẩn nested field
    List<LessonProgress> findByUserAndLesson_Course_Id(User user, Long courseId);
}