package com.warehouse.department.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.port.secondary.CurrentUserServicePort;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final CurrentUserServicePort currentUserServicePort;

    public AuthenticationServiceImpl(final CurrentUserServicePort currentUserServicePort) {
        this.currentUserServicePort = currentUserServicePort;
    }

    @Override
    public UserId currentUser() {
        return currentUserServicePort.getCurrentUserId();
    }
}
