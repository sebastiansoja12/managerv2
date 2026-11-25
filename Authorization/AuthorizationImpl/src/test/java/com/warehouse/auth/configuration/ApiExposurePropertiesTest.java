package com.warehouse.auth.configuration;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ApiExposurePropertiesTest {

    private final ApiExposureProperties properties = new ApiExposureProperties();

    @Test
    void shouldNotExposeDevicePairingWithoutAuthentication() {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/api/internal/device-pairings");
        request.setContextPath("/v1/api");

        assertFalse(properties.isPublicEndpoint(request));
    }

    @Test
    void shouldExposeDeviceActivationWithoutAuthentication() {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/api/internal/device-activations");
        request.setContextPath("/v1/api");

        assertTrue(properties.isPublicEndpoint(request));
    }

    @Test
    void shouldRecognizeMcpPathsWithoutMatchingSimilarPrefixes() {
        assertTrue(properties.isMcpPath(request("/v1/api/mcp")));
        assertTrue(properties.isMcpPath(request("/v1/api/mcp/sse")));
        assertFalse(properties.isMcpPath(request("/v1/api/mcp-tools")));
        assertFalse(properties.isMcpPath(request("/v1/api/devices")));
    }

    @Test
    void shouldExposeConfiguredPublicEndpoint() {
        properties.getPublicEndpoints().add(new ApiExposureProperties.PublicEndpoint("GET", "/status"));
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/api/internal/status");
        request.setContextPath("/v1/api");

        assertTrue(properties.isPublicEndpoint(request));
    }

    @Test
    void shouldExposeBillingWebhooksWithoutMatchingSimilarPrefixes() {
        assertTrue(properties.isPublicPath(request("/v1/api/billing/webhooks/stripe")));
        assertFalse(properties.isPublicPath(request("/v1/api/billing/webhooks-admin")));
        assertFalse(properties.isPublicPath(request("/v1/api/billing/payments")));
    }

    @Test
    void shouldExposeUserInvitationsWithoutAuthentication() {
        assertTrue(properties.isPublicPath(request("/v1/api/auth/invitations/verify")));
        assertTrue(properties.isPublicPath(request("/v1/api/auth/invitations/accept")));
        assertFalse(properties.isPublicPath(request("/v1/api/auth/invitations-admin")));
    }

    @Test
    void shouldProvideSecurityMatchersForPublicPaths() {
        assertArrayEquals(new String[]{"/billing/webhooks", "/billing/webhooks/**", "/auth/invitations",
                        "/auth/invitations/**"},
                properties.publicSecurityMatchers());
    }

    private MockHttpServletRequest request(final String uri) {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", uri);
        request.setContextPath("/v1/api");
        return request;
    }
}
