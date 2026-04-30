package com.khait_academy.backend.mapper;

import com.khait_academy.backend.dto.request.ParentRequest;
import com.khait_academy.backend.dto.response.ParentResponse;
import com.khait_academy.backend.entities.Parent;
import com.khait_academy.backend.enums.ParentStatus;

import java.util.stream.Collectors;

public class ParentMapper {

    // ================= CREATE =================
    public static Parent toEntity(ParentRequest request) {

        ParentStatus status;

        try {
            status = (request.getStatus() != null)
                    ? ParentStatus.valueOf(request.getStatus().toUpperCase())
                    : ParentStatus.ACTIVE;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid parent status");
        }

        return Parent.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(status)
                .build();
    }

    // ================= RESPONSE =================
    public static ParentResponse toResponse(Parent parent) {

        if (parent == null) return null;

        return ParentResponse.builder()
                .id(parent.getId())

                // USER
                .userId(parent.getUser() != null ? parent.getUser().getId() : null)
                .fullName(parent.getFullName())
                .email(parent.getUser() != null ? parent.getUser().getEmail() : null)

                // PROFILE
                .phone(parent.getPhone())
                .address(parent.getAddress())

                // CHILDREN
                .totalStudents(
                        parent.getStudents() != null ? parent.getStudents().size() : 0
                )
                .students(
                        parent.getStudents() == null ? null :
                                parent.getStudents()
                                        .stream()
                                        .map(StudentMapper::toResponse)
                                        .collect(Collectors.toSet())
                )

                // STATUS
                .status(parent.getStatus() != null ? parent.getStatus().name() : null)

                // AUDIT
                .createdAt(parent.getCreatedAt())
                .updatedAt(parent.getUpdatedAt())

                .build();
    }

    // ================= UPDATE =================
    public static void updateEntity(Parent parent, ParentRequest request) {

        if (request.getFullName() != null) {
            parent.setFullName(request.getFullName());
        }

        if (request.getPhone() != null) {
            parent.setPhone(request.getPhone());
        }

        if (request.getAddress() != null) {
            parent.setAddress(request.getAddress());
        }

        if (request.getStatus() != null) {
            parent.setStatus(
                    ParentStatus.valueOf(request.getStatus().toUpperCase())
            );
        }
    }
}