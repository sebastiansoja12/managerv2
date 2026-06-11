package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warehouse.tracking.domain.enumeration.TrackingEnvironment;
import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;

import io.github.resilience4j.retry.RetryConfig;

class InPostTrackingClientTest {

    @Test
    void shouldSendRequiredQueryParametersAndHeadersAndParseResponse() {
        final TestClient testClient = client(3);
        testClient.server().expect(requestTo("https://stage-api.inpost-group.com/tracking/v1/parcels"
                        + "?trackingNumbers=TRACK123456&trackingNumbers=TRACK654321"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer access-token"))
                .andExpect(header("x-inpost-event-version", "V1"))
                .andExpect(header("X-Request-Id", "request-id"))
                .andRespond(withSuccess("{\"parcels\":[{\"trackingNumber\":\"TRACK123456\","
                        + "\"events\":[]}]}", MediaType.APPLICATION_JSON));

        final InPostTrackingResponse response = testClient.client().track(configuration(),
                List.of("TRACK123456", "TRACK654321"), "access-token", "request-id");

        assertEquals("TRACK123456", response.parcels().get(0).trackingNumber());
        testClient.server().verify();
    }

    @Test
    void shouldNotRetryPermanentHttpErrors() {
        for (final HttpStatus status : List.of(HttpStatus.BAD_REQUEST, HttpStatus.UNAUTHORIZED,
                HttpStatus.FORBIDDEN, HttpStatus.NOT_FOUND)) {
            final TestClient testClient = client(3);
            testClient.server().expect(requestTo("https://stage-api.inpost-group.com/tracking/v1/parcels"
                            + "?trackingNumbers=TRACK123456"))
                    .andRespond(withStatus(status));

            assertThrows(TrackingException.class, () -> testClient.client().track(configuration(),
                    List.of("TRACK123456"), "access-token", "request-id"));
            testClient.server().verify();
        }
    }

    @Test
    void shouldRetryRateLimitAndTemporaryServerErrorsUpToConfiguredMaximum() {
        for (final HttpStatus status : List.of(HttpStatus.TOO_MANY_REQUESTS,
                HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.SERVICE_UNAVAILABLE)) {
            final TestClient testClient = client(3);
            testClient.server().expect(times(3), requestTo("https://stage-api.inpost-group.com/tracking/v1/parcels"
                            + "?trackingNumbers=TRACK123456"))
                    .andRespond(withStatus(status));

            assertThrows(TrackingException.class, () -> testClient.client().track(configuration(),
                    List.of("TRACK123456"), "access-token", "request-id"));
            testClient.server().verify();
        }
    }

    @Test
    void shouldRetryTimeoutAndRejectMalformedResponse() {
        final TestClient timeoutClient = client(3);
        timeoutClient.server().expect(times(3), requestTo("https://stage-api.inpost-group.com/tracking/v1/parcels"
                        + "?trackingNumbers=TRACK123456"))
                .andRespond(request -> {
                    throw new ResourceAccessException("read timed out");
                });

        assertThrows(TrackingException.class, () -> timeoutClient.client().track(configuration(),
                List.of("TRACK123456"), "access-token", "request-id"));
        timeoutClient.server().verify();

        final TestClient malformedClient = client(3);
        malformedClient.server().expect(requestTo("https://stage-api.inpost-group.com/tracking/v1/parcels"
                        + "?trackingNumbers=TRACK123456"))
                .andRespond(withSuccess("{malformed", MediaType.APPLICATION_JSON));
        assertThrows(TrackingException.class, () -> malformedClient.client().track(configuration(),
                List.of("TRACK123456"), "access-token", "request-id"));
        malformedClient.server().verify();
    }

    private TestClient client(final int maxAttempts) {
        final RestClient.Builder builder = RestClient.builder();
        final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        final RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .waitDuration(Duration.ofMillis(1))
                .retryOnException(exception -> exception instanceof InPostRequestException
                        && ((InPostRequestException) exception).isRetryable())
                .build();
        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return new TestClient(new InPostTrackingClient(builder.build(), objectMapper, retryConfig), server);
    }

    private TrackingIntegrationConfiguration configuration() {
        return new TrackingIntegrationConfiguration(UUID.randomUUID(), TrackingProviderId.INPOST, true,
                java.util.Map.of("environment", TrackingEnvironment.STAGE.name(),
                        "clientId", "client-id", "clientSecret", "client-secret"));
    }

    private record TestClient(InPostTrackingClient client, MockRestServiceServer server) {
    }
}
