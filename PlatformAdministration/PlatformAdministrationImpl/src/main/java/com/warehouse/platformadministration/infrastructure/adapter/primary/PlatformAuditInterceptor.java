package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.platformadministration.application.port.primary.PlatformAuditPort;
import com.warehouse.platformadministration.application.port.primary.command.RecordAuditedRequestCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Slf4j
public class PlatformAuditInterceptor implements HandlerInterceptor {

    private static final Set<String> AUDITED_METHODS = Set.of("POST", "PUT", "PATCH", "DELETE");

    private static final String PLATFORM_AUTH_PATH = "/platform/auth/";

    private static final String IMPERSONATION_PATH = "/platform/auth/impersonations";

    private final PlatformAuditPort platformAuditPort;

    public PlatformAuditInterceptor(final PlatformAuditPort platformAuditPort) {
        this.platformAuditPort = platformAuditPort;
    }

    @Override
    public void afterCompletion(@NonNull final HttpServletRequest request,
                                @NonNull final HttpServletResponse response,
                                @NonNull final Object handler,
                                final Exception exception) {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        if (!AUDITED_METHODS.contains(request.getMethod()) || isExcludedAuthPath(path)) {
            return;
        }
        try {
            platformAuditPort.record(new RecordAuditedRequestCommand(request.getMethod(), path, response.getStatus(),
                    exception != null));
        } catch (RuntimeException auditException) {
            log.error("Could not record platform audit entry for {} {}", request.getMethod(), path, auditException);
        }
    }

    private boolean isExcludedAuthPath(final String path) {
        return path.startsWith(PLATFORM_AUTH_PATH) && !path.startsWith(IMPERSONATION_PATH);
    }
}
