package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.enumeration.TrackingEnvironment;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;

public class InPostOAuthClient {

    private static final String TRACKING_SCOPE = "api:tracking:read";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public InPostOAuthClient(final RestClient restClient, final ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    InPostTokenResponse requestToken(final TrackingIntegrationConfiguration configuration,
                                     final String requestId) {
        final MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("scope", TRACKING_SCOPE);
        form.add("client_id", configuration.getValue("clientId"));
        form.add("client_secret", configuration.getValue("clientSecret"));
        try {
            return restClient.post()
                    .uri(TrackingEnvironment.valueOf(configuration.getValue("environment")).getTokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("X-Request-Id", requestId)
                    .body(form)
                    .exchange((request, response) -> {
                        final int status = response.getStatusCode().value();
                        if (status >= 200 && status < 300) {
                            final InPostTokenResponse tokenResponse = objectMapper.readValue(
                                    response.getBody(), InPostTokenResponse.class);
                            if (tokenResponse == null
                                    || tokenResponse.accessToken() == null || tokenResponse.accessToken().isBlank()
                                    || tokenResponse.expiresIn() <= 0) {
                                throw failure(TrackingErrorCode.INVALID_PROVIDER_RESPONSE, 502,
                                        "InPost returned an invalid OAuth response", false);
                            }
                            return tokenResponse;
                        }
                        throw oauthFailure(status);
                    });
        } catch (final ResourceAccessException exception) {
            if (hasCause(exception, JsonProcessingException.class)) {
                throw failure(TrackingErrorCode.INVALID_PROVIDER_RESPONSE, 502,
                        "InPost returned an invalid OAuth response", false);
            }
            throw failure(TrackingErrorCode.TIMEOUT, 504,
                    "InPost authentication timed out", true);
        } catch (final RestClientException exception) {
            throw failure(TrackingErrorCode.INVALID_PROVIDER_RESPONSE, 502,
                    "InPost returned an invalid OAuth response", false);
        }
    }

    private InPostRequestException oauthFailure(final int status) {
        if (status == 401) {
            return failure(TrackingErrorCode.AUTHENTICATION_FAILURE, 502,
                    "InPost rejected the configured credentials", false);
        }
        if (status == 403) {
            return failure(TrackingErrorCode.MISSING_PERMISSION, 502,
                    "InPost credentials do not have the required tracking scope", false);
        }
        if (status == 429) {
            return failure(TrackingErrorCode.RATE_LIMIT, 503,
                    "InPost authentication rate limit was exceeded", true);
        }
        if (status >= 500) {
            return failure(TrackingErrorCode.TEMPORARY_UNAVAILABLE, 503,
                    "InPost authentication is temporarily unavailable", true);
        }
        return failure(TrackingErrorCode.AUTHENTICATION_FAILURE, 502,
                "InPost rejected the OAuth request", false);
    }

    private InPostRequestException failure(final TrackingErrorCode errorCode,
                                           final int applicationStatus,
                                           final String message,
                                           final boolean retryable) {
        return new InPostRequestException(errorCode, applicationStatus, message, retryable);
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
