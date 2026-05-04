package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.QuestionRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.QuestionResponse;
import com.khait_academy.backend.services.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * CREATE QUESTION
     */
    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> create(
            @Valid @RequestBody QuestionRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<QuestionResponse>builder()
                        .success(true)
                        .message("Create question success")
                        .data(questionService.create(request))
                        .build()
        );
    }

    /*UPDATE QUESTION */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest request
    ) {

        QuestionResponse response =
                questionService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<QuestionResponse>builder()
                        .success(true)
                        .message("Question updated successfully")
                        .data(response)
                        .build()
        );
    }

    /**
     * GET QUESTIONS BY QUIZ
     */
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getByQuiz(
            @PathVariable Long quizId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<QuestionResponse>>builder()
                        .success(true)
                        .message("Get questions success")
                        .data(questionService.getByQuiz(quizId))
                        .build()
        );
    }

    /**
     * DELETE QUESTION
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable Long id
    ) {

        questionService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Delete question success")
                        .data("OK")
                        .build()
        );
    }
}