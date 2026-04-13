package com.khait_academy.backend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResponse {

    private Long id;
    private String title;
    private String description;
    private Long lessonId;
    private String lessonTitle;
    private LocalDateTime dueDate;
    private Double maxScore;
}