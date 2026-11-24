package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.provider.ApiKeyProvider;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import java.util.Optional;
import com.warehouse.auth.domain.service.ApiEncoderImpl;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserPortImplTest {

    private final ApiKeyEncoder apiKeyEncoder = apiKeyEncoder();

    @Test
    void shouldStoreAuthenticationHashAndPassSecretForEncryptedStorage() {
        final UserService userService = mock(UserService.class);
        final UserId userId = new UserId(17L);
        final UserPort userPort = new UserPortImpl(userService, mock(AuthenticationService.class), apiKeyEncoder);

        final String generatedApiKey = userPort.regenerateApiKey(userId);

        final ArgumentCaptor<String> storedApiKey = ArgumentCaptor.forClass(String.class);
        verify(userService).issueApiKey(eq(userId), storedApiKey.capture(), eq(generatedApiKey), any(Instant.class));
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

    @Test
    void shouldRevealOnlyOwnActiveApiKey() {
        final UserId userId = new UserId(17L);
        final UserService userService = mock(UserService.class);
        final AuthenticationService authentication = mock(AuthenticationService.class);
        final UserPort port = new UserPortImpl(userService, authentication, apiKeyEncoder);
        final OrganizationUser user = OrganizationUser.of(userId, "test", "password", "Test", "User",
                "user@example.com", OrganizationUser.Role.USER, new DepartmentId(1L), null);
        user.issueApiKey("stored-hash", Instant.now());
        when(authentication.currentUserId()).thenReturn(userId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userService.findApiKeySecret(userId)).thenReturn(Optional.of("raw-secret"));

        assertThat(port.revealApiKey(userId)).isEqualTo("raw-secret");
        assertThatThrownBy(() -> port.revealApiKey(new UserId(99L))).isInstanceOf(ProblemDetailsException.class)
                .satisfies(exception -> assertThat(((ProblemDetailsException) exception).getStatus()).isEqualTo(403));

        when(userService.findApiKeySecret(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> port.revealApiKey(userId)).isInstanceOf(ProblemDetailsException.class)
                .satisfies(exception -> assertThat(((ProblemDetailsException) exception).getStatus()).isEqualTo(409));

        user.changeApiKey(null);
        assertThatThrownBy(() -> port.revealApiKey(userId)).isInstanceOf(ProblemDetailsException.class)
                .satisfies(exception -> assertThat(((ProblemDetailsException) exception).getStatus()).isEqualTo(404));
    }

    private static ApiKeyEncoder apiKeyEncoder() {
        final ApiKeyProvider apiKeyProvider = new ApiKeyProvider();
        apiKeyProvider.setKey("test-api-key-secret");
        return new ApiEncoderImpl(apiKeyProvider);
    }
}
