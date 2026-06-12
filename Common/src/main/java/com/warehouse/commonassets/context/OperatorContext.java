package com.warehouse.commonassets.context;

import java.util.Collections;
import java.util.function.Supplier;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

@Component
public class OperatorContext {

    public <T> T runAs(final OperatorId operatorId, final Supplier<T> operation) {
        return runWithPrincipal(operatorId, operatorId, operation);
    }

    public <T> T runAs(final OperatorId operatorId, final UserId userId, final Supplier<T> operation) {
        return runWithPrincipal(operatorId, userId, operation);
    }

    private <T> T runWithPrincipal(final OperatorId operatorId,
                                   final Object principal,
                                   final Supplier<T> operation) {
        final Authentication previousAuthentication = SecurityContextHolder.getContext().getAuthentication();
        login(principal, operatorId);
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

    public void runAs(final OperatorId operatorId, final UserId userId, final Runnable operation) {
        runAs(operatorId, userId, () -> {
            operation.run();
            return null;
        });
    }

    public void assignOperator(final OperatorId operatorId) {
        login(operatorId, operatorId);
    }

    public void clear() {
        SecurityContextHolder.clearContext();
    }

    private void login(final Object principal, final OperatorId operatorId) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                principal,
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
