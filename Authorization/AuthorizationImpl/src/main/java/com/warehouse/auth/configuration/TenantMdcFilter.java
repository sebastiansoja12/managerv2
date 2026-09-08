package com.warehouse.auth.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.service.JwtDecodeService;
import com.warehouse.auth.domain.vo.DecodedApiTenant;
import com.warehouse.exceptionhandler.exception.RestException;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantMdcFilter extends OncePerRequestFilter {

    private final JwtDecodeService jwtDecodeService;

    private final ApiExposureProperties apiExposureProperties;

    private final AuthCookieService authCookieService;

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort;

    private final Logger log = org.slf4j.LoggerFactory.getLogger("");

    private static final List<String> WHITELIST = List.of(
            "/v1/api/swagger-ui",
            "/v1/api/v3/api-docs",
            "/v1/api/swagger-resources",
            "/v1/api/webjars",
            "/v1/api/auth/login",
            "/v1/api/auth/refresh",
            "/v1/api/auth/logout",
            "/v1/api/auth/csrf",
            "/v1/api/maintenance",
            "/v1/api/reroutes",
            "/v1/api/platform/auth/login",
            "/v1/api/platform/auth/refresh",
            "/v1/api/platform/auth/logout",
            "/v1/api/departments/read-sync",
            "/v1/api/internal/shipments/read-sync",
            "/internal/shipments/read-sync",
            "/v1/api/admin/async-jobs",
            "/v1/api/ws",
            "/ws"
    );

    public TenantMdcFilter(final JwtDecodeService jwtDecodeService,
                           final ApiExposureProperties apiExposureProperties,
                           final AuthCookieService authCookieService,
                           final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort) {
        this.jwtDecodeService = jwtDecodeService;
        this.apiExposureProperties = apiExposureProperties;
        this.authCookieService = authCookieService;
        this.platformOwnerAuthenticationPort = platformOwnerAuthenticationPort;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, java.io.IOException {

        try {
            final String uri = request.getRequestURI();

            if (isWhitelisted(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (authCookieService.readAccessToken(request).isEmpty()) {
                final Optional<PlatformOwner> platformOwner = authenticatePlatformOwner(request);
                if (platformOwner.isPresent()) {
                    initPlatformOwnerMdc(platformOwner.get(), request);
                    log.info("Incoming {} platform owner request", request.getMethod());
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            final DecodedApiTenant tenantInfo = authenticateWithJwt(request, response);
            if (tenantInfo == null) {
                log.warn("Authentication failed for URI: {}", uri);
                if (!response.isCommitted()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Authentication failed");
                }
                return;
            }
            initMdc(tenantInfo, request);
            log.info("Incoming {} request", request.getMethod());
            filterChain.doFilter(request, response);

        } catch (final Exception e) {
            log.error("Error during request processing", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
        } finally {
            MDC.clear();
        }
    }

    private void initMdc(final DecodedApiTenant tenant, final HttpServletRequest request) {
        MDC.put("operator", tenant.operatorId() != null ? tenant.operatorId().toString() : "N/A");
        MDC.put("user", tenant.userId().value().toString());
        MDC.put("username", tenant.username());
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private Optional<PlatformOwner> authenticatePlatformOwner(final HttpServletRequest request) {
        try {
            return authCookieService.readPlatformOwnerAccessToken(request)
                    .flatMap(platformOwnerAuthenticationPort::authenticate);
        } catch (final RuntimeException exception) {
            log.warn("Invalid or expired platform owner access token");
            return Optional.empty();
        }
    }

    private void initPlatformOwnerMdc(final PlatformOwner platformOwner, final HttpServletRequest request) {
        MDC.put("operator", "PLATFORM");
        MDC.put("user", "platform-owner-" + platformOwner.getPlatformOwnerId().value());
        MDC.put("username", platformOwner.getUsername());
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private DecodedApiTenant authenticateWithJwt(final HttpServletRequest request,
                                                  final HttpServletResponse response) throws IOException, java.io.IOException {

        final String token = authCookieService.readAccessToken(request).orElse(null);
        if (token == null) {
            unauthorized(response, "Missing access token");
            return null;
        }
        try {
            return jwtDecodeService.decodeJwt(token);
        } catch (final RestException exception) {
            unauthorized(response, "Invalid or expired access token");
        }
        return null;
    }

    private void unauthorized(final HttpServletResponse response, final String message) throws IOException, java.io.IOException {
        log.warn("Unauthorized request: {}", message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }

    private boolean isWhitelisted(final HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        final String uri = request.getRequestURI();
        if (apiExposureProperties.isPublicEndpoint(request)) {
            return true;
        }
        return WHITELIST.stream().anyMatch(uri::startsWith);
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return apiExposureProperties.isPairKeyController(request);
    }
}
