package com.warehouse.commonassets.logging;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

public class UserLogConverter extends SecurityContextLogConverter {

    @Override
    protected String fromToken(final UsernameTenantPasswordAuthenticationToken token) {
        // runAs without a user authenticates the operator itself
        return token.getPrincipal() instanceof UserId userId ? String.valueOf(userId.value()) : NOT_AVAILABLE;
    }

    @Override
    protected String mdcKey() {
        return "user";
    }
}
