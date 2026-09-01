package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;

public record PlatformOwnerAccountResponse(Long platformOwnerId,
                                           String username,
                                           String firstName,
                                           String lastName,
                                           String email,
                                           boolean active,
                                           Instant createdAt) {
}
