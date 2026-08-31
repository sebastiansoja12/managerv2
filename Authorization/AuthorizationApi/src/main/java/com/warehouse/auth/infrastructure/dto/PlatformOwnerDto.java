package com.warehouse.auth.infrastructure.dto;

import java.time.Instant;

public record PlatformOwnerDto(Long platformOwnerId, String username, String firstName, String lastName, String email,
                               boolean active, Instant createdAt) {
}
