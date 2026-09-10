package com.warehouse.routetracker.infrastructure.adapter.secondary;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.routetracker.domain.vo.UserContext;

public class CurrentUserServiceAdapter implements CurrentUserServicePort {

    @Override
    public UserId getCurrentUserId() {
        return getCurrentUserContext().userId();
    }

    @Override
    public OperatorId getCurrentOperatorId() {
        return getCurrentUserContext().operatorId();
    }

    @Override
    public DepartmentId getCurrentDepartmentId() {
        return getCurrentUserContext().departmentId();
    }

    private UserContext getCurrentUserContext() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserContext userContext) {
            return userContext;
        }
        throw new AuthenticationCredentialsNotFoundException("Current user context is not available");
    }
}
