package com.khait_academy.backend.dto.response;

import com.khait_academy.backend.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {

    private Long id;

    private Long userId;
    private String userName;     // ✅ thêm dòng này

    private Long lessonId;
    private String lessonTitle;  // ✅ thêm dòng này

    private AttendanceStatus status;
    private LocalDateTime attendedAt;
    private String note;
}