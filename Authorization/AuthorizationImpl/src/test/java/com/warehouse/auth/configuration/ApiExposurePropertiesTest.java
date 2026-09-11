package com.warehouse.auth.configuration;

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

    private MockHttpServletRequest request(final String uri) {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", uri);
        request.setContextPath("/v1/api");
        return request;
    }
}
