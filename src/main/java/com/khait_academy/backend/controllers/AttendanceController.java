package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.AttendanceRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     *  CHECK-IN
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(
            @RequestBody AttendanceRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<AttendanceResponse>builder()
                        .success(true)
                        .message("Check-in success")
                        .data(attendanceService.checkIn(request))
                        .build()
        );
    }

    /**
     *  GET BY LESSON
     */
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getByLesson(
            @PathVariable Long lessonId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<AttendanceResponse>>builder()
                        .success(true)
                        .data(attendanceService.getByLesson(lessonId))
                        .build()
        );
    }

    /**
     *  GET BY USER
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getByUser(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<AttendanceResponse>>builder()
                        .success(true)
                        .data(attendanceService.getByUser(userId))
                        .build()
        );
    }

    /**
     *  DELETE
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {

        attendanceService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .build()
        );
    }
}