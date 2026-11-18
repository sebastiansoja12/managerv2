package com.warehouse.platformadministration.infrastructure.adapter.primary;

import tools.jackson.databind.ObjectMapper;
import com.warehouse.platformadministration.application.port.primary.MaintenanceModePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class MaintenanceModeInterceptor implements HandlerInterceptor {

    private final MaintenanceModePort maintenanceModePort;

    private final ObjectMapper objectMapper;

    public MaintenanceModeInterceptor(final MaintenanceModePort maintenanceModePort, final ObjectMapper objectMapper) {
        this.maintenanceModePort = maintenanceModePort;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request,
                             @NonNull final HttpServletResponse response,
                             @NonNull final Object handler) throws IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final boolean blocked;
        try {
            blocked = maintenanceModePort.isBlocked(path, request.getMethod());
        } catch (RuntimeException exception) {
            log.error("Could not resolve maintenance mode for {}", path, exception);
            return true;
        }
        if (!blocked) {
            return true;
        }
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("maintenance", true);
        body.put("message", maintenanceModePort.current().message());
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Retry-After", "60");
        objectMapper.writeValue(response.getOutputStream(), body);
        return false;
    }
}
