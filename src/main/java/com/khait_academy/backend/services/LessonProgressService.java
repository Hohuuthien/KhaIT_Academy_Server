package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.LessonProgressRequest;
import com.khait_academy.backend.dto.response.LessonProgressResponse;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.LessonProgress;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.mapper.LessonProgressMapper;
import com.khait_academy.backend.repositories.LessonProgressRepository;
import com.khait_academy.backend.repositories.LessonRepository;
import com.khait_academy.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonProgressService {

    private final LessonProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    // ================= UPDATE PROGRESS =================
    @Transactional
    public LessonProgressResponse updateProgress(String email, LessonProgressRequest request) {

        // 1. lấy user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. lấy lesson
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // 3. tìm progress
        LessonProgress progress = progressRepository
                .findByUserAndLesson(user, lesson)
                .orElse(null);

        // 4. create hoặc update
        if (progress == null) {
            progress = LessonProgressMapper.toEntity(request, user, lesson);
        } else {
            LessonProgressMapper.updateEntity(progress, request);
        }

        // 5. save
        progressRepository.save(progress);

        // 6. trả response
        return LessonProgressMapper.toResponse(progress);
    }

    // ================= GET PROGRESS BY COURSE =================
    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getProgressByCourse(String email, Long courseId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return progressRepository
                .findByUserAndLesson_Course_Id(user, courseId)
                .stream()
                .map(LessonProgressMapper::toResponse)
                .toList();
    }

    // ================= GET PROGRESS BY LESSON =================
    @Transactional(readOnly = true)
    public LessonProgressResponse getProgressByLesson(String email, Long lessonId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        LessonProgress progress = progressRepository
                .findByUserAndLesson(user, lesson)
                .orElse(null);

        if (progress == null) {
            // chưa học → trả default
            return LessonProgressResponse.builder()
                    .lessonId(lessonId)
                    .progress(0.0)
                    .completed(false)
                    .lastPosition(0L)
                    .build();
        }

        return LessonProgressMapper.toResponse(progress);
    }
}