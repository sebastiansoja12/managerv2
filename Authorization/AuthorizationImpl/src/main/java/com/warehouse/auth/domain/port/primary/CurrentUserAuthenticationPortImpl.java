package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.CurrentUserAuthenticationService;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.commonassets.identificator.UserId;

public class CurrentUserAuthenticationPortImpl implements CurrentUserAuthenticationPort {

    private final CurrentUserAuthenticationService currentUserAuthenticationService;

    public CurrentUserAuthenticationPortImpl(final CurrentUserAuthenticationService currentUserAuthenticationService) {
        this.currentUserAuthenticationService = currentUserAuthenticationService;
    }

    @Override
    public UserId getCurrentUserId() {
        return currentUserAuthenticationService.getCurrentUserId();
    }

    @Override
    public OrganizationUser getCurrentUser() {
        return currentUserAuthenticationService.getCurrentUser();
    }

    @Override
    public CurrentUserAuthentication getCurrentUserAuthentication() {
        return currentUserAuthenticationService.getCurrentUserAuthentication();
    }
}
