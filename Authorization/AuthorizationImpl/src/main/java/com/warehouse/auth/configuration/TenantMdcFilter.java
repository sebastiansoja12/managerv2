package com.warehouse.auth.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.PlatformOwnerAuthenticationFilter;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantMdcFilter extends OncePerRequestFilter {

    private final ApiExposureProperties apiExposureProperties;

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

    public TenantMdcFilter(final ApiExposureProperties apiExposureProperties) {
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

            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof UsernameTenantPasswordAuthenticationToken token) {
                initMdc(token, request);
                log.info("Incoming {} request", request.getMethod());
                filterChain.doFilter(request, response);
                return;
            }
            if (authentication != null && authentication.getPrincipal() instanceof PlatformOwnerId platformOwnerId) {
                initPlatformOwnerMdc(platformOwnerId, request);
                log.info("Incoming {} platform owner request", request.getMethod());
                filterChain.doFilter(request, response);
                return;
            }

            log.warn("Authentication failed for URI: {}", uri);
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authentication failed");
            }

        } catch (final Exception e) {
            log.error("Error during request processing", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
        } finally {
            MDC.clear();
        }
    }

    private void initMdc(final UsernameTenantPasswordAuthenticationToken token, final HttpServletRequest request) {
        MDC.put("operator", token.getOperatorId() != null ? String.valueOf(token.getOperatorId().value()) : "N/A");
        MDC.put("user", token.getPrincipal() instanceof UserId userId ? String.valueOf(userId.value()) : "N/A");
        MDC.put("username", requestAttribute(request, JwtAuthenticationFilter.USERNAME_ATTRIBUTE));
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private void initPlatformOwnerMdc(final PlatformOwnerId platformOwnerId, final HttpServletRequest request) {
        MDC.put("operator", "PLATFORM");
        MDC.put("user", "platform-owner-" + platformOwnerId.value());
        MDC.put("username", requestAttribute(request, PlatformOwnerAuthenticationFilter.PLATFORM_OWNER_USERNAME_ATTRIBUTE));
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private static String requestAttribute(final HttpServletRequest request, final String name) {
        return request.getAttribute(name) instanceof String value ? value : "N/A";
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
        return apiExposureProperties.isPairKeyController(request) || apiExposureProperties.isMcpPath(request)
                || apiExposureProperties.isExternalPath(request);
    }
}
