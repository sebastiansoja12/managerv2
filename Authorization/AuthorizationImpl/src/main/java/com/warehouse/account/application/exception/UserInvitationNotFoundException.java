package com.warehouse.account.application.exception;

public class UserInvitationNotFoundException extends RuntimeException {

    public UserInvitationNotFoundException() {
        super("User invitation not found");
    }
}
