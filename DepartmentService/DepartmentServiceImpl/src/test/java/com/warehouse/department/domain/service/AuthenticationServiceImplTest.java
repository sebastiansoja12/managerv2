package com.warehouse.department.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.port.secondary.CurrentUserServicePort;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private CurrentUserServicePort currentUserServicePort;

    @Test
    void shouldReturnCurrentUserId() {
        final UserId currentUserId = new UserId(1001L);
        when(currentUserServicePort.getCurrentUserId()).thenReturn(currentUserId);
        final AuthenticationService authenticationService = new AuthenticationServiceImpl(currentUserServicePort);

        final UserId result = authenticationService.currentUser();

        assertEquals(currentUserId, result);
        verify(currentUserServicePort).getCurrentUserId();
    }
}
