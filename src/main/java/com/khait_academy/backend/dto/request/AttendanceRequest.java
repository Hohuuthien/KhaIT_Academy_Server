package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.AttendanceStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRequest {

    private Long userId;
    private Long lessonId;
    private AttendanceStatus status;
    private String note;
}