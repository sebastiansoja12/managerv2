package com.warehouse.auth.configuration;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityOperatorContextProvider implements OperatorContextProvider {

    @Override
    public Optional<OperatorDetails> currentContext() {
        return currentAuthentication().map(authentication -> new OperatorDetails(
                authentication.getOperatorId(),
                authentication.getPrincipal() instanceof final UserId userId ? userId : null,
                authentication.getDepartmentId()));
    }

    private Optional<UsernameTenantPasswordAuthenticationToken> currentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(UsernameTenantPasswordAuthenticationToken.class::isInstance)
                .map(UsernameTenantPasswordAuthenticationToken.class::cast);
    }
}
