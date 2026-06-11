package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.enumeration.TrackingEnvironment;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

public class InPostTrackingClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(InPostTrackingClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final RetryConfig retryConfig;

    public InPostTrackingClient(final RestClient restClient,
                                final ObjectMapper objectMapper,
                                final RetryConfig retryConfig) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.retryConfig = retryConfig;
    }

    public InPostTrackingResponse track(final TrackingIntegrationConfiguration configuration,
                                        final List<String> trackingNumbers,
                                        final String accessToken,
                                        final String requestId) {
        final Retry retry = Retry.of("inpost-tracking", retryConfig);
        retry.getEventPublisher().onRetry(event -> LOGGER.warn(
                "Retrying InPost tracking request attempt={} requestId={}",
                event.getNumberOfRetryAttempts(), requestId));
        try {
            return Retry.decorateSupplier(retry,
                    () -> execute(configuration, trackingNumbers, accessToken, requestId)).get();
        } catch (final InPostRequestException exception) {
            throw new TrackingException(exception.getErrorCode(), exception.getApplicationStatus(),
                    exception.getMessage(), requestId);
        }
    }

    private InPostTrackingResponse execute(final TrackingIntegrationConfiguration configuration,
                                           final List<String> trackingNumbers,
                                           final String accessToken,
                                           final String requestId) {
        final URI uri = UriComponentsBuilder.fromUriString(
                        TrackingEnvironment.valueOf(configuration.getValue("environment")).getBaseUrl())
                .path("/tracking/v1/parcels")
                .queryParam("trackingNumbers", trackingNumbers.toArray())
                .build()
                .encode()
                .toUri();
        final long startedAt = System.nanoTime();
        try {
            return restClient.get()
                    .uri(uri)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("x-inpost-event-version", "V1")
                    .header("X-Request-Id", requestId)
                    .exchange((request, response) -> {
                        final int status = response.getStatusCode().value();
                        LOGGER.info("InPost tracking request completed status={} durationMs={} requestId={}",
                                status, elapsedMilliseconds(startedAt), requestId);
                        if (status >= 200 && status < 300) {
                            final InPostTrackingResponse trackingResponse = objectMapper.readValue(
                                    response.getBody(), InPostTrackingResponse.class);
                            if (trackingResponse == null || trackingResponse.parcels() == null) {
                                throw failure(TrackingErrorCode.INVALID_PROVIDER_RESPONSE, 502,
                                        "InPost returned an invalid tracking response", false);
                            }
                            return trackingResponse;
                        }
                        throw responseFailure(status);
                    });
        } catch (final ResourceAccessException exception) {
            if (hasCause(exception, JsonProcessingException.class)) {
                throw failure(TrackingErrorCode.INVALID_PROVIDER_RESPONSE, 502,
                        "InPost returned an invalid tracking response", false);
            }
            LOGGER.warn("InPost tracking request timed out durationMs={} requestId={}",
                    elapsedMilliseconds(startedAt), requestId);
            throw failure(TrackingErrorCode.TIMEOUT, 504, "InPost tracking request timed out", true);
        } catch (final RestClientException exception) {
            throw failure(TrackingErrorCode.INVALID_PROVIDER_RESPONSE, 502,
                    "InPost returned an invalid tracking response", false);
        }
    }

    private InPostRequestException responseFailure(final int status) {
        return switch (status) {
            case 400, 422 -> failure(TrackingErrorCode.INVALID_TRACKING_NUMBER, 400,
                    "InPost rejected the tracking number", false);
            case 401 -> failure(TrackingErrorCode.AUTHENTICATION_FAILURE, 502,
                    "InPost authentication failed", false);
            case 403 -> failure(TrackingErrorCode.MISSING_PERMISSION, 502,
                    "InPost credentials do not have the required tracking scope", false);
            case 404 -> failure(TrackingErrorCode.NOT_FOUND, 404,
                    "The parcel was not found in InPost", false);
            case 429 -> failure(TrackingErrorCode.RATE_LIMIT, 503,
                    "InPost request limit was exceeded", true);
            default -> status >= 500
                    ? failure(TrackingErrorCode.TEMPORARY_UNAVAILABLE, 503,
                    "InPost tracking is temporarily unavailable", true)
                    : failure(TrackingErrorCode.INVALID_PROVIDER_RESPONSE, 502,
                    "InPost returned an unexpected response", false);
        };
    }

    private InPostRequestException failure(final TrackingErrorCode errorCode,
                                           final int applicationStatus,
                                           final String message,
                                           final boolean retryable) {
        return new InPostRequestException(errorCode, applicationStatus, message, retryable);
    }

    private long elapsedMilliseconds(final long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000;
    }

    private boolean hasCause(final Throwable exception, final Class<? extends Throwable> causeType) {
        Throwable cause = exception;
        while (cause != null) {
            if (causeType.isInstance(cause)) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
