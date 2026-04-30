package com.khait_academy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.UserStatus;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true)
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== AUTH =====
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // ===== PROFILE =====
    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "phone", length = 20)
    private String phone;

    // ===== STATUS (thay boolean active) =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    // ===== ROLE =====
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        indexes = {
            @Index(name = "idx_user_roles_user", columnList = "user_id"),
            @Index(name = "idx_user_roles_role", columnList = "role_id")
        }
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // ===== OAUTH READY =====
    @Column(name = "provider", length = 50)
    private String provider; // LOCAL, GOOGLE, FACEBOOK
}