package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.pallet.domain.vo.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CurrentUserServiceAdapter implements CurrentUserServicePort {

    @Override
    public Optional<UserContext> currentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserContext userContext) {
            return Optional.of(userContext);
        }
        return Optional.empty();
    }
}
