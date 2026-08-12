package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.tracking.domain.enumeration.TrackingEnvironment;
import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;

class InPostOAuthClientTest {

    @Test
    void shouldRequestClientCredentialsTokenWithTrackingScope() {
        final RestClient.Builder builder = RestClient.builder();
        final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        final MultiValueMap<String, String> expectedForm = new LinkedMultiValueMap<>();
        expectedForm.add("grant_type", "client_credentials");
        expectedForm.add("scope", "api:tracking:read");
        expectedForm.add("client_id", "client-id");
        expectedForm.add("client_secret", "client-secret");
        server.expect(requestTo("https://stage-api.inpost-group.com/oauth2/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Request-Id", "request-id"))
                .andExpect(content().formData(expectedForm))
                .andRespond(withSuccess("{\"access_token\":\"token\",\"token_type\":\"Bearer\","
                        + "\"expires_in\":3600}", MediaType.APPLICATION_JSON));
        final InPostOAuthClient client = new InPostOAuthClient(builder.build(), new ObjectMapper());

        final InPostTokenResponse response = client.requestToken(configuration(), "request-id");

        assertEquals("token", response.accessToken());
        assertEquals(3600, response.expiresIn());
        server.verify();
    }

    @Test
    void shouldMapAuthenticationAndPermissionFailuresWithoutExposingResponsePayload() {
        for (final HttpStatus status : new HttpStatus[]{HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN}) {
            final RestClient.Builder builder = RestClient.builder();
            final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
            server.expect(requestTo("https://stage-api.inpost-group.com/oauth2/token"))
                    .andRespond(withStatus(status).body("client_secret=must-not-leak"));
            final InPostOAuthClient client = new InPostOAuthClient(builder.build(), new ObjectMapper());

            final InPostRequestException exception = assertThrows(InPostRequestException.class,
                    () -> client.requestToken(configuration(), "request-id"));

            assertEquals(status == HttpStatus.UNAUTHORIZED
                    ? TrackingErrorCode.AUTHENTICATION_FAILURE
                    : TrackingErrorCode.MISSING_PERMISSION, exception.getErrorCode());
            server.verify();
        }
    }

    private TrackingIntegrationConfiguration configuration() {
        return new TrackingIntegrationConfiguration(UUID.randomUUID(), TrackingProviderId.INPOST, true,
                java.util.Map.of("environment", TrackingEnvironment.STAGE.name(),
                        "clientId", "client-id", "clientSecret", "client-secret"));
    }
}
