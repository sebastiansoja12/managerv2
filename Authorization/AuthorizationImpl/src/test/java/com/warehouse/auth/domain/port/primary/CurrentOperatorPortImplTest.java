package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.AuthenticationServiceImpl;
import com.warehouse.auth.domain.service.RefreshTokenGenerator;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CurrentOperatorPortImplTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetOperatorIdFromTenantAuthenticationToken() {
        final OperatorId operatorId = OperatorId.of(10001L);
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                new UserId(2100L),
                operatorId,
                "token",
                List.of()
        ));
        SecurityContextHolder.setContext(securityContext);
        final AuthenticationService authenticationService = new AuthenticationServiceImpl(
                mock(RefreshTokenRepository.class),
                mock(RefreshTokenGenerator.class),
                mock(UserRepository.class)
        );
        final CurrentOperatorPort currentOperatorPort = new CurrentOperatorPortImpl(authenticationService);

        final OperatorId currentOperatorId = currentOperatorPort.getCurrentOperatorId();

        assertEquals(operatorId, currentOperatorId);
    }
}
