package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.port.secondary.TrackingTokenServicePort;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

public class InPostTokenManager implements TrackingTokenServicePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(InPostTokenManager.class);
    private static final long EXPIRY_SKEW_SECONDS = 30;

    private final InPostOAuthClient oauthClient;
    private final RetryConfig retryConfig;
    private final Clock clock;
    private final Map<UUID, CachedToken> tokens = new HashMap<>();

    public InPostTokenManager(final InPostOAuthClient oauthClient,
                              final RetryConfig retryConfig,
                              final Clock clock) {
        this.oauthClient = oauthClient;
        this.retryConfig = retryConfig;
        this.clock = clock;
    }

    @Override
    public synchronized String accessToken(final TrackingIntegrationConfiguration configuration,
                                           final String requestId) {
        final int configurationFingerprint = Objects.hash(
                configuration.getValue("environment"), configuration.getValue("clientId"),
                configuration.getValue("clientSecret"));
        final CachedToken cachedToken = tokens.get(configuration.getConfigurationId());
        if (cachedToken != null
                && cachedToken.configurationFingerprint() == configurationFingerprint
                && cachedToken.validAt(clock.instant())) {
            return cachedToken.accessToken();
        }

        final Retry retry = Retry.of("inpost-oauth", retryConfig);
        retry.getEventPublisher().onRetry(event -> LOGGER.warn(
                "Retrying InPost OAuth request attempt={} requestId={}",
                event.getNumberOfRetryAttempts(), requestId));
        try {
            final InPostTokenResponse tokenResponse = Retry.decorateSupplier(retry,
                    () -> oauthClient.requestToken(configuration, requestId)).get();
            final long validitySeconds = Math.max(1, tokenResponse.expiresIn() - EXPIRY_SKEW_SECONDS);
            tokens.put(configuration.getConfigurationId(), new CachedToken(
                    tokenResponse.accessToken(), clock.instant().plusSeconds(validitySeconds), configurationFingerprint));
            return tokenResponse.accessToken();
        } catch (final InPostRequestException exception) {
            throw new TrackingException(exception.getErrorCode(), exception.getApplicationStatus(),
                    exception.getMessage(), requestId);
        }
    }

    @Override
    public synchronized void invalidate(final UUID configurationId) {
        tokens.remove(configurationId);
    }

    private record CachedToken(String accessToken, Instant usableUntil, int configurationFingerprint) {

        private boolean validAt(final Instant instant) {
            return usableUntil.isAfter(instant);
        }
    }
}
