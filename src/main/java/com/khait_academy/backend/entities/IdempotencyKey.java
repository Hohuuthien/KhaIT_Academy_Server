package com.khait_academy.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "idempotency_keys")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyKey {
    @Id
    @Column(length = 100)
    private String key;

    @Column(nullable = false)
    private String endpoint;

    @Lob
    private String response;

    private Integer statusCode;

    private LocalDateTime createdAt;
}
