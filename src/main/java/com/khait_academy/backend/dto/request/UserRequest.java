package com.khait_academy.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @Email(message = "Email không hợp lệ")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String fullName;

    private String avatarUrl;

    // ROLE chuẩn hệ thống (ADMIN / TEACHER / STUDENT / PARENT)
    private Set<Long> roleIds;
}