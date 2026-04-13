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
    private String userName;

    private Long lessonId;
    private String lessonTitle;

    private AttendanceStatus status;
    private LocalDateTime attendedAt;

    private String note;
}