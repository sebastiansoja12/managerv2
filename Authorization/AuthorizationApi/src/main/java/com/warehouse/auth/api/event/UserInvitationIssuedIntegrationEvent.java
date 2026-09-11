package com.warehouse.auth.api.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.Objects;

@IntegrationEventType(value = "user.invitation.issued", version = 1)
public record UserInvitationIssuedIntegrationEvent(
        UserId userId,
        String email,
        String firstName,
        String username,
        String invitationUrl,
        String language,
        Instant expiresAt,
        Instant timestamp) implements IntegrationEvent, IntegrationEventKey {

    public UserInvitationIssuedIntegrationEvent {
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(invitationUrl, "Invitation URL cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.userId.value());
    }
}
