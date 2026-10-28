package com.warehouse.commonassets.logging;

import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

public class OperatorLogConverter extends SecurityContextLogConverter {

    @Override
    protected String fromToken(final UsernameTenantPasswordAuthenticationToken token) {
        return token.getOperatorId() == null ? NOT_AVAILABLE : String.valueOf(token.getOperatorId().value());
    }

    @Override
    protected String mdcKey() {
        return "operator";
    }
}
