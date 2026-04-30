package com.khait_academy.backend.entities;

import com.khait_academy.backend.common.BaseEntity;
import com.khait_academy.backend.enums.RoleName;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "roles",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_role_name", columnNames = "name")
    },
    indexes = {
        @Index(name = "idx_role_name", columnList = "name")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // ===== ROLE NAME =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoleName name;

    // ===== DESCRIPTION =====
    @Column(length = 255)
    private String description;

    // ===== USER RELATION =====
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    @Builder.Default
    private Set<User> users = new HashSet<>();

    // ===== PERMISSION RELATION (NEW - QUAN TRỌNG) =====
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"),
        indexes = {
            @Index(name = "idx_role_perm_role", columnList = "role_id"),
            @Index(name = "idx_role_perm_perm", columnList = "permission_id")
        }
    )
    @BatchSize(size = 10)
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    // ===== HELPER =====
    public String getAuthority() {
        return this.name.name(); // ROLE_ADMIN
    }

    // ===== HELPER ADD/REMOVE =====
    public void addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.getRoles().add(this);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
    }
}