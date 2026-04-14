package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequest {

    @NotNull
    private Long assignmentId;

    @NotNull
    private String fileUrl;
}