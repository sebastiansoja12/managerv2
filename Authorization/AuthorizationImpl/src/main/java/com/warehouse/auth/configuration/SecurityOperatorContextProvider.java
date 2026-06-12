package com.warehouse.auth.configuration;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityOperatorContextProvider implements OperatorContextProvider {

    @Override
    public Optional<OperatorId> currentOperatorId() {
        return currentAuthentication().map(UsernameTenantPasswordAuthenticationToken::getOperatorId);
    }

    @Override
    public Optional<UserId> currentUserId() {
        return currentAuthentication()
                .map(UsernameTenantPasswordAuthenticationToken::getPrincipal)
                .filter(UserId.class::isInstance)
                .map(UserId.class::cast);
    }

    @Override
    public Optional<DepartmentId> currentDepartmentId() {
        return currentAuthentication().map(UsernameTenantPasswordAuthenticationToken::getDepartmentId);
    }

    private Optional<UsernameTenantPasswordAuthenticationToken> currentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(UsernameTenantPasswordAuthenticationToken.class::isInstance)
                .map(UsernameTenantPasswordAuthenticationToken.class::cast);
    }
}
