package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.platformadministration.application.port.primary.MaintenanceModePort;
import com.warehouse.platformadministration.domain.model.MaintenanceMode;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MaintenanceModeInterceptorTest {

    private final MaintenanceModePort maintenanceModePort = mock(MaintenanceModePort.class);

    private final MaintenanceModeInterceptor interceptor = new MaintenanceModeInterceptor(maintenanceModePort,
            new ObjectMapper());

    @Test
    void shouldRespondWithServiceUnavailableWhenRequestIsBlocked() throws Exception {
        when(maintenanceModePort.isBlocked("/shipments", "GET")).thenReturn(true);
        when(maintenanceModePort.current()).thenReturn(new MaintenanceMode(true, "Aktualizacja", "owner", Instant.now()));
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final boolean proceed = interceptor.preHandle(request("GET", "/shipments"), response, new Object());

        assertThat(proceed).isFalse();
        assertThat(response.getStatus()).isEqualTo(503);
        assertThat(response.getContentAsString()).contains("\"maintenance\":true").contains("Aktualizacja");
    }

    @Test
    void shouldPassRequestWhenNotBlocked() throws Exception {
        when(maintenanceModePort.isBlocked("/platform/system/status", "GET")).thenReturn(false);

        assertThat(interceptor.preHandle(request("GET", "/platform/system/status"), new MockHttpServletResponse(),
                new Object())).isTrue();
    }

    @Test
    void shouldPassRequestWhenMaintenanceStateCannotBeRead() throws Exception {
        when(maintenanceModePort.isBlocked("/shipments", "GET")).thenThrow(new IllegalStateException("db down"));

        assertThat(interceptor.preHandle(request("GET", "/shipments"), new MockHttpServletResponse(), new Object()))
                .isTrue();
    }

    private MockHttpServletRequest request(final String method, final String path) {
        final MockHttpServletRequest request = new MockHttpServletRequest(method, "/v2/api" + path);
        request.setContextPath("/v2/api");
        return request;
    }
}
