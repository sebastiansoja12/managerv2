package com.warehouse.account.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record UserInvitationApiResponse(String username, Instant expiresAt) {
}
