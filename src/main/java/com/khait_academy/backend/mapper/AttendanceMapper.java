package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.AttendanceRequest;
import com.khait_academy.backend.dto.response.AttendanceResponse;
import com.khait_academy.backend.entities.Attendance;
import com.khait_academy.backend.entities.Lesson;
import com.khait_academy.backend.entities.Student;

public class AttendanceMapper {

    // ================= CREATE =================
    public static Attendance toEntity(
            AttendanceRequest request,
            Student student,
            Lesson lesson
    ) {

        return Attendance.builder()
                .student(student)
                .lesson(lesson)
                .status(request.getStatus())
                .attendedAt(request.getAttendedAt())
                .note(request.getNote())
                .checkedBy(request.getCheckedBy())
                .build();
    }

    // ================= RESPONSE =================
    public static AttendanceResponse toResponse(Attendance a) {

        return AttendanceResponse.builder()
                .id(a.getId())

                // student
                .studentId(a.getStudent() != null ? a.getStudent().getId() : null)
                .studentName(
                        a.getStudent() != null && a.getStudent().getUser() != null
                                ? a.getStudent().getUser().getFullName()
                                : null
                )

                // lesson
                .lessonId(a.getLesson() != null ? a.getLesson().getId() : null)
                .lessonTitle(a.getLesson() != null ? a.getLesson().getTitle() : null)

                // attendance
                .status(a.getStatus())
                .attendedAt(a.getAttendedAt())
                .note(a.getNote())

                .checkedBy(a.getCheckedBy())

                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }

    // ================= UPDATE =================
    public static void updateEntity(Attendance a, AttendanceRequest r) {

        if (r.getStatus() != null) a.setStatus(r.getStatus());
        if (r.getAttendedAt() != null) a.setAttendedAt(r.getAttendedAt());
        if (r.getNote() != null) a.setNote(r.getNote());
        if (r.getCheckedBy() != null) a.setCheckedBy(r.getCheckedBy());
    }
}