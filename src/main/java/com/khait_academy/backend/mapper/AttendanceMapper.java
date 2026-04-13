package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.entities.Attendance;

public class AttendanceMapper {

    public static AttendanceResponse toResponse(Attendance entity) {
        return AttendanceResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .userName(entity.getUser().getFullName())
                .lessonId(entity.getLesson().getId())
                .lessonTitle(entity.getLesson().getTitle())
                .status(entity.getStatus())
                .attendedAt(entity.getAttendedAt())
                .note(entity.getNote())
                .build();
    }
}