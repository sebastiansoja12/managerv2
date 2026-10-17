package com.warehouse.auth.infrastructure.dto;

public record PlatformActorDto(Long platformOwnerId, String username, boolean impersonation, Long impersonatedUserId,
                               Long operatorId) {
}
