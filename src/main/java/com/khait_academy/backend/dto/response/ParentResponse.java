package com.khait_academy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.khait_academy.backend.enums.ParentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class ParentResponse {

    // BASIC
    private Long id;
    private Long userId;

    // USER INFO
    private String fullName;
    private String email;

    // PROFILE
    private String phone;
    private String address;

    // RELATION
    private int totalStudents;
    private Set<StudentResponse> students;

    // STATUS
    private ParentStatus status;

    // AUDIT
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}