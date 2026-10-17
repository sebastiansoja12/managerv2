package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserPortImplTest {

    @Test
    void shouldGenerateApiKeyInPrimaryPortAndPassItToUserService() {
        final UserService userService = mock(UserService.class);
        final UserId userId = new UserId(17L);
        final UserPort userPort = new UserPortImpl(userService, mock(AuthenticationService.class));

        final String generatedApiKey = userPort.regenerateApiKey(userId);

        final ArgumentCaptor<String> apiKey = ArgumentCaptor.forClass(String.class);
        verify(userService).changeApiKey(eq(userId), apiKey.capture());
        assertThat(generatedApiKey).isEqualTo(apiKey.getValue());
        assertThat(generatedApiKey).startsWith("mgr_").hasSize(47);
        assertThat(generatedApiKey.substring(4)).matches("[A-Za-z0-9_-]{43}");
    }

    @Test
    void shouldDeleteApiKeyThroughUserService() {
        final UserService userService = mock(UserService.class);
        final UserId userId = new UserId(17L);
        final UserPort userPort = new UserPortImpl(userService, mock(AuthenticationService.class));

        userPort.deleteApiKey(userId);

        verify(userService).changeApiKey(userId, null);
    }
}
