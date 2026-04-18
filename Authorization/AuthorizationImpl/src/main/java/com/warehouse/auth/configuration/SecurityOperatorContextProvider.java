package com.warehouse.auth.configuration;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityOperatorContextProvider implements OperatorContextProvider {

    @Override
    public Optional<OperatorId> currentOperatorId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernameTenantPasswordAuthenticationToken token) {
            return Optional.ofNullable(token.getOperatorId());
        }

        return Optional.empty();
    }
}
