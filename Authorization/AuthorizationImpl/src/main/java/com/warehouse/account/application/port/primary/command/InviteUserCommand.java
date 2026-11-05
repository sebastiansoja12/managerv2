package com.warehouse.account.application.port.primary.command;

import com.warehouse.commonassets.identificator.UserId;

public record InviteUserCommand(
        UserId userId,
        String username,
        String firstName,
        String email,
        String language) {
}
