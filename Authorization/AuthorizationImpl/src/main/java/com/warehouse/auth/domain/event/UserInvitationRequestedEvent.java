package com.warehouse.auth.domain.event;

import com.warehouse.commonassets.identificator.UserId;

public record UserInvitationRequestedEvent(
        UserId userId,
        String username,
        String firstName,
        String email,
        String language) implements UserEvent {
}
