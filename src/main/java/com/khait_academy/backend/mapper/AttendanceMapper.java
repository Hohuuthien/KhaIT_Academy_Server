package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.entities.Attendance;
import com.khait_academy.backend.enums.AttendanceStatus;

import java.util.List;

public class AttendanceMapper {

    private AttendanceMapper() {
        // prevent instantiation
    }

    public static AttendanceResponse toResponse(Attendance entity) {

        if (entity == null) {
            return null;
        }

        return AttendanceResponse.builder()
                .id(entity.getId())

                // USER
                .userId(getUserId(entity))
                .userName(getUserName(entity))

                // LESSON
                .lessonId(getLessonId(entity))
                .lessonTitle(getLessonTitle(entity))

                // ✅ FIX QUAN TRỌNG: trả về ENUM, KHÔNG phải String
                .status(getStatus(entity))

                .attendedAt(entity.getAttendedAt())
                .note(entity.getNote())
                .build();
    }

    // ================= HELPER METHODS =================

    private static Long getUserId(Attendance entity) {
        return entity.getUser() != null ? entity.getUser().getId() : null;
    }

    private static String getUserName(Attendance entity) {
        return entity.getUser() != null ? entity.getUser().getFullName() : null;
    }

    private static Long getLessonId(Attendance entity) {
        return entity.getLesson() != null ? entity.getLesson().getId() : null;
    }

    private static String getLessonTitle(Attendance entity) {
        return entity.getLesson() != null ? entity.getLesson().getTitle() : null;
    }

    // ✅ RETURN ENUM (chuẩn với DTO)
    private static AttendanceStatus getStatus(Attendance entity) {
        return entity.getStatus();
    }

    // ================= MAP LIST =================

    public static List<AttendanceResponse> toList(List<Attendance> entities) {
        return entities.stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }
}