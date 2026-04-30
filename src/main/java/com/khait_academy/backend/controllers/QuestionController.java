// package com.khait_academy.backend.controllers;

// import com.khait_academy.backend.dto.request.QuestionRequest;
// import com.khait_academy.backend.dto.response.*;
// import com.khait_academy.backend.services.QuestionService;

// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/v1/questions")
// @RequiredArgsConstructor
// public class QuestionController {

//     private final QuestionService questionService;

//     @PostMapping
//     public QuestionResponse create(@RequestBody QuestionRequest req) {
//         return questionService.create(req);
//     }

//     @GetMapping("/lesson/{lessonId}")
//     public List<QuestionResponse> getByLesson(@PathVariable Long lessonId) {
//         return questionService.getByLesson(lessonId);
//     }

//     @DeleteMapping("/{id}")
//     public void delete(@PathVariable Long id) {
//         questionService.delete(id);
//     }
// }