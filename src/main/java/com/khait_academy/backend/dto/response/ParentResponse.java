package com.khait_academy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class ParentResponse {

    private Long id;

    // USER
    private Long userId;
    private String fullName;
    private String email;

    // PROFILE
    private String phone;
    private String address;

    // CHILDREN
    private Integer totalStudents;
    private Set<StudentResponse> students;

    // STATUS
    private String status;

    // AUDIT
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}