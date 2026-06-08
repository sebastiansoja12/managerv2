package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.vo.UserContext;
import com.warehouse.routetracker.infrastructure.adapter.secondary.CurrentUserServiceAdapter;

class CurrentUserServiceAdapterTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnCurrentUserOperatorAndDepartmentIdsFromSecurityContext() {
        final UserContext userContext = new UserContext(
                new UserId(42L), OperatorId.of(7L), new DepartmentId(10L));
        final UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                userContext,
                null,
                List.of()
        );
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        final CurrentUserServiceAdapter currentUserService = new CurrentUserServiceAdapter();

        final UserId currentUserId = currentUserService.getCurrentUserId();
        final OperatorId currentOperatorId = currentUserService.getCurrentOperatorId();
        final DepartmentId currentDepartmentId = currentUserService.getCurrentDepartmentId();

        assertThat(currentUserId).isEqualTo(new UserId(42L));
        assertThat(currentOperatorId).isEqualTo(OperatorId.of(7L));
        assertThat(currentDepartmentId).isEqualTo(new DepartmentId(10L));
    }
}
