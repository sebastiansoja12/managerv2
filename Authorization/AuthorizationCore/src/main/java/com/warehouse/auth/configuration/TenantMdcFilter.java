package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.service.ApiKeyService;
import com.warehouse.auth.domain.vo.DecodedApiTenant;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class TenantMdcFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    private static final List<String> API_KEY_ENDPOINTS = List.of(
            "/v2/api/devices",
            "/v2/api/device-pairing",
            "/v2/api/deliveries"
    );

    private static final List<String> WHITELIST = List.of(
            "/v2/api/swagger-ui",
            "/v2/api/v3/api-docs",
            "/v2/api/swagger-resources",
            "/v2/api/webjars",
            "/v2/api/auth/login"
    );

    public TenantMdcFilter(final ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, java.io.IOException {

        initMdc(request);

        try {
            final String uri = request.getRequestURI();

            if (isWhitelisted(uri)) {
                filterChain.doFilter(request, response);
                return;
            }

            final DecodedApiTenant tenantInfo = isApiKeyEndpoint(uri)
                    ? authenticateWithApiKey(request, response)
                    : authenticateWithJwt(request, response);

            if (tenantInfo == null) {
                log.warn("Authentication failed for URI: {}", uri);
                if (!response.isCommitted()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Authentication failed");
                }
                return;
            }

            setMdcFromTenant(tenantInfo);
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

    private void initMdc(final HttpServletRequest request) {
        MDC.put("tenant", "N/A");
        MDC.put("user", "N/A");
        MDC.put("username", "N/A");
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private DecodedApiTenant authenticateWithApiKey(final HttpServletRequest request,
                                                    final HttpServletResponse response) throws IOException, java.io.IOException {
        final String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null || apiKey.isBlank()) {
            unauthorized(response, "Missing or invalid api key");
            return null;
        }
        try {
            return apiKeyService.decodeApiKey(apiKey);
        } catch (final IllegalArgumentException e) {
            unauthorized(response, e.getMessage());
        } catch (final Exception e) {
            internalError(response, "Failed to decode API Key");
        }
        return null;
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

    private boolean isWhitelisted(final String uri) {
        return WHITELIST.stream().anyMatch(uri::startsWith);
    }

    private boolean isApiKeyEndpoint(final String uri) {
        return API_KEY_ENDPOINTS.stream().anyMatch(uri::startsWith);
    }
}
