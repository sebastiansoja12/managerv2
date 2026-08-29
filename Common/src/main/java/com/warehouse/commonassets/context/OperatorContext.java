package com.warehouse.commonassets.context;

import java.util.Collections;
import java.util.function.Supplier;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

@Component
public class OperatorContext {

    public <T> T runAs(final OperatorId operatorId, final Supplier<T> operation) {
        return runWithPrincipal(operatorId, operatorId, null, operation);
    }

    public <T> T runAs(final OperatorId operatorId, final UserId userId, final Supplier<T> operation) {
        return runWithPrincipal(operatorId, userId, null, operation);
    }

    public <T> T runAs(final OperatorId operatorId,
                       final UserId userId,
                       final DepartmentId departmentId,
                       final Supplier<T> operation) {
        return runWithPrincipal(operatorId, userId, departmentId, operation);
    }

    private <T> T runWithPrincipal(final OperatorId operatorId,
                                   final Object principal,
                                   final DepartmentId departmentId,
                                   final Supplier<T> operation) {
        final Authentication previousAuthentication = SecurityContextHolder.getContext().getAuthentication();
        login(principal, operatorId, departmentId);
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

    public void runAs(final OperatorId operatorId,
                      final UserId userId,
                      final DepartmentId departmentId,
                      final Runnable operation) {
        runAs(operatorId, userId, departmentId, () -> {
            operation.run();
            return null;
        });
    }

    public void assignOperator(final OperatorId operatorId) {
        assignOperatorContext(operatorId, null, null);
    }

    public void assignOperatorContext(final OperatorId operatorId,
                                      final UserId userId,
                                      final DepartmentId departmentId) {
        login(userId == null ? operatorId : userId, operatorId, departmentId);
    }

    public void clear() {
        SecurityContextHolder.clearContext();
    }

    private void login(final Object principal,
                       final OperatorId operatorId,
                       final DepartmentId departmentId) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                principal,
                operatorId,
                departmentId,
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
