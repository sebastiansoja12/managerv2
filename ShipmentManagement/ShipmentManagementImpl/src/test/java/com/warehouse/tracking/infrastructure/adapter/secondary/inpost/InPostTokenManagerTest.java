package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.warehouse.tracking.domain.enumeration.TrackingEnvironment;
import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;

import io.github.resilience4j.retry.RetryConfig;

class InPostTokenManagerTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    @AfterEach
    void shutdownExecutor() {
        executor.shutdownNow();
    }

    @Test
    void shouldFetchCacheAndRefreshTokenAfterExpiry() {
        final InPostOAuthClient oauthClient = mock(InPostOAuthClient.class);
        final MutableClock clock = new MutableClock(Instant.parse("2026-08-12T10:00:00Z"));
        final TrackingIntegrationConfiguration configuration = configuration();
        when(oauthClient.requestToken(configuration, "request-1"))
                .thenReturn(new InPostTokenResponse("first-token", "Bearer", 40))
                .thenReturn(new InPostTokenResponse("second-token", "Bearer", 40));

        final InPostTokenManager manager = new InPostTokenManager(oauthClient, retryConfig(), clock);

        assertEquals("first-token", manager.accessToken(configuration, "request-1"));
        assertEquals("first-token", manager.accessToken(configuration, "request-1"));
        clock.advance(Duration.ofSeconds(11));
        assertEquals("second-token", manager.accessToken(configuration, "request-1"));
        verify(oauthClient, times(2)).requestToken(configuration, "request-1");
    }

    @Test
    void shouldMapAuthenticationFailureWithoutRetry() {
        final InPostOAuthClient oauthClient = mock(InPostOAuthClient.class);
        final TrackingIntegrationConfiguration configuration = configuration();
        when(oauthClient.requestToken(eq(configuration), any(String.class)))
                .thenThrow(new InPostRequestException(TrackingErrorCode.AUTHENTICATION_FAILURE, 502,
                        "InPost rejected the configured credentials", false));
        final InPostTokenManager manager = new InPostTokenManager(
                oauthClient, retryConfig(), Clock.systemUTC());

        assertThrows(TrackingException.class, () -> manager.accessToken(configuration, "request-2"));
        verify(oauthClient).requestToken(configuration, "request-2");
    }

    @Test
    void shouldFetchOnlyOneTokenForConcurrentRequests() throws Exception {
        final InPostOAuthClient oauthClient = mock(InPostOAuthClient.class);
        final TrackingIntegrationConfiguration configuration = configuration();
        when(oauthClient.requestToken(configuration, "request-3"))
                .thenReturn(new InPostTokenResponse("shared-token", "Bearer", 3600));
        final InPostTokenManager manager = new InPostTokenManager(
                oauthClient, retryConfig(), Clock.systemUTC());
        final List<Callable<String>> calls = new ArrayList<>();
        for (int index = 0; index < 24; index++) {
            calls.add(() -> manager.accessToken(configuration, "request-3"));
        }

        final List<Future<String>> results = executor.invokeAll(calls);

        for (final Future<String> result : results) {
            assertEquals("shared-token", result.get());
        }
        verify(oauthClient).requestToken(configuration, "request-3");
    }

    private TrackingIntegrationConfiguration configuration() {
        return new TrackingIntegrationConfiguration(UUID.randomUUID(), TrackingProviderId.INPOST, true,
                java.util.Map.of("environment", TrackingEnvironment.STAGE.name(),
                        "clientId", "client-id", "clientSecret", "client-secret"));
    }

    private RetryConfig retryConfig() {
        return RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(1))
                .retryOnException(exception -> exception instanceof InPostRequestException
                        && ((InPostRequestException) exception).isRetryable())
                .build();
    }

    private static final class MutableClock extends Clock {

        private Instant instant;

        private MutableClock(final Instant instant) {
            this.instant = instant;
        }

        private void advance(final Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(final ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
