package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.provider.ApiKeyProvider;
import com.warehouse.auth.domain.service.ApiEncoderImpl;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserPortImplTest {

    private final ApiKeyEncoder apiKeyEncoder = apiKeyEncoder();

    @Test
    void shouldStoreOnlyHashOfGeneratedApiKey() {
        final UserService userService = mock(UserService.class);
        final UserId userId = new UserId(17L);
        final UserPort userPort = new UserPortImpl(userService, mock(AuthenticationService.class), apiKeyEncoder);

        final String generatedApiKey = userPort.regenerateApiKey(userId);

        final ArgumentCaptor<String> storedApiKey = ArgumentCaptor.forClass(String.class);
        verify(userService).issueApiKey(eq(userId), storedApiKey.capture(), any(Instant.class));
        assertThat(storedApiKey.getValue()).isNotEqualTo(generatedApiKey);
        assertThat(storedApiKey.getValue()).isEqualTo(apiKeyEncoder.encode(userId, generatedApiKey).key());
    }

    @Test
    void shouldGenerateDifferentApiKeysForSameUser() {
        final UserPort userPort = new UserPortImpl(mock(UserService.class), mock(AuthenticationService.class),
                apiKeyEncoder);

        assertThat(userPort.regenerateApiKey(new UserId(17L))).isNotEqualTo(userPort.regenerateApiKey(new UserId(17L)));
    }

    @Test
    void shouldDeleteApiKeyThroughUserService() {
        final UserService userService = mock(UserService.class);
        final UserId userId = new UserId(17L);
        final UserPort userPort = new UserPortImpl(userService, mock(AuthenticationService.class), apiKeyEncoder);

        userPort.deleteApiKey(userId);

        verify(userService).changeApiKey(userId, null);
    }

    private static ApiKeyEncoder apiKeyEncoder() {
        final ApiKeyProvider apiKeyProvider = new ApiKeyProvider();
        apiKeyProvider.setKey("test-api-key-secret");
        return new ApiEncoderImpl(apiKeyProvider);
    }
}
