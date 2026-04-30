package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "permissions",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_permission_name", columnNames = "name")
    },
    indexes = {
        @Index(name = "idx_permission_name", columnList = "name")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== NAME =====
    @Column(nullable = false, length = 100, unique = true)
    private String name;
    // ví dụ: COURSE_CREATE, COURSE_UPDATE, QUIZ_ATTEMPT

    // ===== DESCRIPTION =====
    @Column(length = 255)
    private String description;

    // ===== RELATION =====
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}