package com.warehouse.auth.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.auth.domain.service.ApiKeyService;
import com.warehouse.auth.domain.vo.DecodedApiTenant;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantMdcFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    private final ApiExposureProperties apiExposureProperties;

    private final Logger log = org.slf4j.LoggerFactory.getLogger("");

    private static final List<String> WHITELIST = List.of(
            "/v2/api/swagger-ui",
            "/v2/api/v3/api-docs",
            "/v2/api/swagger-resources",
            "/v2/api/webjars",
            "/v2/api/auth/login"
    );

    public TenantMdcFilter(final ApiKeyService apiKeyService,
                           final ApiExposureProperties apiExposureProperties) {
        this.apiKeyService = apiKeyService;
        this.apiExposureProperties = apiExposureProperties;
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
            JwtContext.clear();
        }
    }

    private void initMdc(final DecodedApiTenant tenant, final HttpServletRequest request) {
        MDC.put("tenant", tenant.departmentCode().value());
        MDC.put("user", tenant.userId().value().toString());
        MDC.put("username", tenant.username());
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private DecodedApiTenant authenticateWithJwt(final HttpServletRequest request,
                                                  final HttpServletResponse response) throws IOException, java.io.IOException {

        final String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            unauthorized(response, "Missing or invalid Authorization header");
            return null;
        }
        try {
            final String token = auth.substring(7);
            JwtContext.setToken(token);
            return apiKeyService.decodeJwt(token);
        } catch (final IllegalArgumentException e) {
            unauthorized(response, e.getMessage());
        } catch (final Exception e) {
            internalError(response, "Failed to decode JWT");
        }
        return null;
    }

    private void setMdcFromTenant(final DecodedApiTenant tenant) {
        MDC.put("tenant", tenant.departmentCode().value());
        MDC.put("user", tenant.userId().value().toString());
        MDC.put("username", tenant.username());
    }

    private void unauthorized(final HttpServletResponse response, final String message) throws IOException, java.io.IOException {
        log.warn("Unauthorized request: {}", message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }

    private void internalError(final HttpServletResponse response, final String message) throws IOException, java.io.IOException {
        log.error(message);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Internal server error");
    }

    private boolean isWhitelisted(final HttpServletRequest request) {
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
