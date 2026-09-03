package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.platformadministration.application.port.primary.PlatformAuditPort;
import com.warehouse.platformadministration.application.port.primary.command.RecordAuditedRequestCommand;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class PlatformAuditInterceptorTest {

    private final PlatformAuditPort platformAuditPort = mock(PlatformAuditPort.class);

    private final PlatformAuditInterceptor interceptor = new PlatformAuditInterceptor(platformAuditPort);

    @Test
    void shouldRecordMutatingRequestWithPathWithoutContext() {
        interceptor.afterCompletion(request("POST", "/operators/10/departments"), response(201), new Object(), null);

        verify(platformAuditPort).record(new RecordAuditedRequestCommand("POST", "/operators/10/departments", 201, false));
    }

    @Test
    void shouldIgnoreReadRequests() {
        interceptor.afterCompletion(request("GET", "/operators/10/users"), response(200), new Object(), null);

        verify(platformAuditPort, never()).record(any());
    }

    @Test
    void shouldIgnorePlatformAuthenticationLifecycle() {
        interceptor.afterCompletion(request("POST", "/platform/auth/login"), response(204), new Object(), null);
        interceptor.afterCompletion(request("POST", "/platform/auth/refresh"), response(204), new Object(), null);

        verify(platformAuditPort, never()).record(any());
    }

    @Test
    void shouldRecordImpersonationStart() {
        interceptor.afterCompletion(request("POST", "/platform/auth/impersonations"), response(200), new Object(), null);

        verify(platformAuditPort).record(new RecordAuditedRequestCommand("POST", "/platform/auth/impersonations", 200, false));
    }

    @Test
    void shouldMarkRequestEndedWithExceptionAsFailed() {
        interceptor.afterCompletion(request("DELETE", "/announcements/active"), response(200), new Object(),
                new IllegalStateException("failure"));

        verify(platformAuditPort).record(new RecordAuditedRequestCommand("DELETE", "/announcements/active", 200, true));
    }

    @Test
    void shouldNotBreakRequestWhenAuditFails() {
        doThrow(new IllegalStateException("database unavailable")).when(platformAuditPort).record(any());

        assertThatCode(() -> interceptor.afterCompletion(request("PUT", "/platform/owners/2/block"), response(204),
                new Object(), null)).doesNotThrowAnyException();
    }

    private MockHttpServletRequest request(final String method, final String path) {
        final MockHttpServletRequest request = new MockHttpServletRequest(method, "/v2/api" + path);
        request.setContextPath("/v2/api");
        return request;
    }

    private MockHttpServletResponse response(final int status) {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(status);
        return response;
    }
}
