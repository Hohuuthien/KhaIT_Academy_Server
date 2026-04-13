package com.khait_academy.backend.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentRequest {

    private String title;
    private String description;
    private Long lessonId;
    private LocalDateTime dueDate;
    private Double maxScore;
}