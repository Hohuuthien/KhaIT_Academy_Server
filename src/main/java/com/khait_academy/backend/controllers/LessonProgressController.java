// package com.khait_academy.backend.controllers;

// import com.khait_academy.backend.dto.request.LessonProgressRequest;
// import com.khait_academy.backend.dto.response.ApiResponse;
// import com.khait_academy.backend.dto.response.LessonProgressResponse;
// import com.khait_academy.backend.services.LessonProgressService;

// import lombok.RequiredArgsConstructor;

// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/v1/progress")
// @RequiredArgsConstructor
// public class LessonProgressController {

//     private final LessonProgressService lessonProgressService;

//     // ================= UPDATE PROGRESS =================
//     @PostMapping
//     public ResponseEntity<ApiResponse<LessonProgressResponse>> updateProgress(
//             @RequestBody LessonProgressRequest request,
//             Authentication authentication
//     ) {
//         String email = extractEmail(authentication);

//         LessonProgressResponse response =
//                 lessonProgressService.updateProgress(email, request);

//         return ResponseEntity.ok(
//                 ApiResponse.<LessonProgressResponse>builder()
//                         .success(true)
//                         .message("Cập nhật tiến độ thành công")
//                         .data(response)
//                         .build()
//         );
//     }

//     // ================= GET BY COURSE =================
//     @GetMapping("/course/{courseId}")
//     public ResponseEntity<ApiResponse<List<LessonProgressResponse>>> getByCourse(
//             @PathVariable Long courseId,
//             Authentication authentication
//     ) {
//         String email = extractEmail(authentication);

//         List<LessonProgressResponse> response =
//                 lessonProgressService.getProgressByCourse(email, courseId);

//         return ResponseEntity.ok(
//                 ApiResponse.<List<LessonProgressResponse>>builder()
//                         .success(true)
//                         .message("Danh sách tiến độ theo course")
//                         .data(response)
//                         .build()
//         );
//     }

//     // ================= GET BY LESSON =================
//     @GetMapping("/lesson/{lessonId}")
//     public ResponseEntity<ApiResponse<LessonProgressResponse>> getByLesson(
//             @PathVariable Long lessonId,
//             Authentication authentication
//     ) {
//         String email = extractEmail(authentication);

//         LessonProgressResponse response =
//                 lessonProgressService.getProgressByLesson(email, lessonId);

//         return ResponseEntity.ok(
//                 ApiResponse.<LessonProgressResponse>builder()
//                         .success(true)
//                         .message("Tiến độ lesson")
//                         .data(response)
//                         .build()
//         );
//     }

//     // ================= SAFE AUTH EXTRACT =================
//     private String extractEmail(Authentication authentication) {
//         if (authentication == null || authentication.getName() == null) {
//             throw new RuntimeException("Unauthorized: missing authentication");
//         }
//         return authentication.getName();
//     }
// }