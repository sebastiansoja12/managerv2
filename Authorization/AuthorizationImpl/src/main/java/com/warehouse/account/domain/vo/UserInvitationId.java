package com.warehouse.account.domain.vo;

import java.util.UUID;

public record UserInvitationId(UUID value) {

    public static UserInvitationId generate() {
        return new UserInvitationId(UUID.randomUUID());
    }
}
