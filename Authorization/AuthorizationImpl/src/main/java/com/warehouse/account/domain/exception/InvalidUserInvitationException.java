package com.warehouse.account.domain.exception;

import com.warehouse.account.domain.enumeration.UserInvitationFailure;

public class InvalidUserInvitationException extends RuntimeException {

    private final UserInvitationFailure failure;

    public InvalidUserInvitationException(final UserInvitationFailure failure) {
        super("User invitation cannot be used: " + failure.name());
        this.failure = failure;
    }

    public UserInvitationFailure getFailure() {
        return failure;
    }
}
