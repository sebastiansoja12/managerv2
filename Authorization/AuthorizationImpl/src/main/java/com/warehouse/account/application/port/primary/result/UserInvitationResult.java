package com.warehouse.account.application.port.primary.result;

import java.time.Instant;

public record UserInvitationResult(String username, Instant expiresAt) {
}
