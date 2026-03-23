package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.commonassets.identificator.OperatorId;

public class CurrentOperatorPortImpl implements CurrentOperatorPort {

    private final AuthenticationService authenticationService;

    public CurrentOperatorPortImpl(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public OperatorId getCurrentOperatorId() {
        return authenticationService.currentOperatorId();
    }
}
