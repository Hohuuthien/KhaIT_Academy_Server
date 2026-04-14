package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long lessonId;

    @NotNull
    private AttendanceStatus status;

    private String note;
}