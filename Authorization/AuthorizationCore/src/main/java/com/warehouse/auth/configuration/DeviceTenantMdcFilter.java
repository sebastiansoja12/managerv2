package com.warehouse.auth.configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.response.DeviceAuthenticationResponseDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ConditionalOnBean(DeviceApiService.class)
public class DeviceTenantMdcFilter extends OncePerRequestFilter {

    private static final List<String> DEVICE_ENDPOINTS = List.of(
            "/v2/api/deliveries",
            "/v2/api/ws"
    );

    private static final String HEADER_DEVICE_PAIR_KEY = "X-DEVICE-PAIR-KEY";

    private final DeviceApiService deviceApiService;

    public DeviceTenantMdcFilter(final DeviceApiService deviceApiService) {
        this.deviceApiService = deviceApiService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        initMdc(request);

        final String pairKey = request.getHeader(HEADER_DEVICE_PAIR_KEY);
        if (pairKey == null || pairKey.isBlank()) {
            unauthorized(response, "Missing device pair key");
            return;
        }

        final DeviceAuthenticationResponseDto authentication;
        try {
            authentication = this.deviceApiService.authenticateByPairKey(pairKey);
        } catch (final RuntimeException ex) {
            internalError(response, "Device authentication service failure");
            return;
        }

        if (authentication == null || !Boolean.TRUE.equals(authentication.value())) {
            unauthorized(response, "Invalid device credentials");
            return;
        }

        final String department = authentication.departmentCode() != null
                ? authentication.departmentCode().value()
                : null;
        if (department == null || department.isBlank()) {
            unauthorized(response, "Missing device department in authenticated context");
            return;
        }

        final Long userId = authentication.userId() != null ? authentication.userId().value() : null;
        final String deviceId = authentication.deviceId() != null ? authentication.deviceId().value() : null;
        final String username = authentication.username() != null ? authentication.username().value() : null;

        setMdcFromDevice(userId, deviceId, username, department);
        setDeviceAuthentication(resolvePrincipal(userId, deviceId), department, pairKey);
        log.info("Incoming {} request (device-auth)", request.getMethod());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            JwtContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        if (uri.endsWith(".wsdl") || uri.endsWith(".xsd")) {
            return true;
        }
        return DEVICE_ENDPOINTS.stream().noneMatch(uri::startsWith);
    }

    private void initMdc(final HttpServletRequest request) {
        MDC.put("tenant", "N/A");
        MDC.put("user", "N/A");
        MDC.put("username", "N/A");
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private void setMdcFromDevice(final Long userId, final String deviceId, final String username, final String department) {
        MDC.put("tenant", department);
        if (userId != null) {
            MDC.put("user", userId.toString());
        } else if (deviceId != null) {
            MDC.put("user", deviceId);
        }
        if (username != null && !username.isBlank()) {
            MDC.put("username", username);
        }
    }

    private Object resolvePrincipal(final Long userId, final String deviceId) {
        if (userId != null) {
            return new UserId(userId);
        }
        if (deviceId != null) {
            return "device:" + deviceId;
        }
        return "device:unknown";
    }

    private void setDeviceAuthentication(final Object principal, final String departmentCode, final String pairKey) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        final UsernameTenantPasswordAuthenticationToken authToken =
                new UsernameTenantPasswordAuthenticationToken(
                        principal,
                        new DepartmentCode(departmentCode),
                        pairKey,
                        Collections.emptyList());
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }

    private void unauthorized(final HttpServletResponse response, final String message) throws IOException {
        log.warn("Device authentication failed: {}", message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }

    private void internalError(final HttpServletResponse response, final String message) throws IOException {
        log.error(message);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Internal server error");
    }
}
