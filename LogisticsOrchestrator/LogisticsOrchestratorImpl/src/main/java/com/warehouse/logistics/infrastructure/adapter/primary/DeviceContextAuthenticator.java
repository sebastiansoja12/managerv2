package com.warehouse.logistics.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.response.DeviceAuthenticationResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceContextAuthenticator {

    private final DeviceApiService deviceApiService;

    public DeviceContextAuthenticator(final DeviceApiService deviceApiService) {
        this.deviceApiService = deviceApiService;
    }

    public AuthenticatedDeviceContext authenticateByPairKey(final String pairKey) {
        if (pairKey == null || pairKey.isBlank()) {
            throw new IllegalArgumentException("Missing device pair key");
        }

        initRequestMdc();

        final DeviceAuthenticationResponseDto authentication = deviceApiService.authenticateByPairKey(pairKey);
        if (authentication == null || !Boolean.TRUE.equals(authentication.value())) {
            throw new IllegalArgumentException("Invalid device pair key");
        }

        final String departmentCode = authentication.departmentCode() != null
                ? authentication.departmentCode().value()
                : null;
        if (departmentCode == null || departmentCode.isBlank()) {
            throw new IllegalArgumentException("Missing department for authenticated device");
        }

        final Long userId = authentication.userId() != null ? authentication.userId().value() : null;
        final String username = authentication.username() != null ? authentication.username().value() : null;
        final String deviceId = authentication.deviceId() != null ? authentication.deviceId().value() : null;

        setSecurityContext(pairKey, departmentCode, userId, deviceId);
        setMdc(departmentCode, userId, username, deviceId);
        log.info("Device context initialized for device {}, user {}, department {}",
                deviceId, username != null ? username : userId, departmentCode);
        return new AuthenticatedDeviceContext(pairKey, deviceId, departmentCode, userId, username);
    }

    public void clear() {
        MDC.clear();
        SecurityContextHolder.clearContext();
    }

    private void setSecurityContext(final String pairKey,
                                    final String departmentCode,
                                    final Long userId,
                                    final String deviceId) {
        final Object principal = userId != null ? new UserId(userId) : "device:" + fallback(deviceId);
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                principal,
                new DepartmentCode(departmentCode),
                pairKey,
                Collections.emptyList()));
        SecurityContextHolder.setContext(context);
    }

    private void setMdc(final String departmentCode,
                        final Long userId,
                        final String username,
                        final String deviceId) {
        MDC.put("operator", "N/A");
        MDC.put("user", userId != null ? userId.toString() : fallback(deviceId));
        MDC.put("username", username != null ? username : "device:" + fallback(deviceId));
    }

    private void initRequestMdc() {
        final HttpServletRequest request = currentHttpRequest();
        MDC.put("operator", "N/A");
        MDC.put("user", "N/A");
        MDC.put("username", "N/A");
        MDC.put("uri", request != null ? request.getRequestURL().toString() : "SOAP");
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request != null ? request.getMethod() : "SOAP");
    }

    private HttpServletRequest currentHttpRequest() {
        final TransportContext transportContext = TransportContextHolder.getTransportContext();
        if (transportContext == null || !(transportContext.getConnection() instanceof HttpServletConnection connection)) {
            return null;
        }
        return connection.getHttpServletRequest();
    }

    private String fallback(final String value) {
        return value != null && !value.isBlank() ? value : "unknown";
    }

    public record AuthenticatedDeviceContext(String pairKey,
                                             String deviceId,
                                             String departmentCode,
                                             Long userId,
                                             String username) {
    }
}
