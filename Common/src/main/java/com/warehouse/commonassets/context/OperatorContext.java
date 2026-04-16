package com.warehouse.commonassets.context;

import java.util.Collections;
import java.util.function.Supplier;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

@Component
public class OperatorContext {

    public <T> T runAs(final OperatorId operatorId, final Supplier<T> operation) {
        final Authentication previousAuthentication = SecurityContextHolder.getContext().getAuthentication();
        login(operatorId);
        try {
            return operation.get();
        } finally {
            logout(previousAuthentication);
        }
    }

    public void runAs(final OperatorId operatorId, final Runnable operation) {
        runAs(operatorId, () -> {
            operation.run();
            return null;
        });
    }

    private void login(final OperatorId operatorId) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                operatorId,
                operatorId,
                null,
                Collections.emptyList()
        ));
        SecurityContextHolder.setContext(context);
    }

    private void logout(final Authentication previousAuthentication) {
        if (previousAuthentication == null) {
            SecurityContextHolder.clearContext();
        } else {
            final SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(previousAuthentication);
            SecurityContextHolder.setContext(context);
        }
    }
}
