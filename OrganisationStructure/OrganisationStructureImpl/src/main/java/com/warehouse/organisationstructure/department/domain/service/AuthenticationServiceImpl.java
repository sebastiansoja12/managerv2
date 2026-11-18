package com.warehouse.organisationstructure.department.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.department.application.port.secondary.CurrentUserServicePort;

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
