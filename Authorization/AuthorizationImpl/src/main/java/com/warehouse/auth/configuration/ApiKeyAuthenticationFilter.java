package com.warehouse.auth.configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String BEARER_REALM = "Bearer realm=\"manager-mcp\"";

    private final UserService userService;

    private final ApiKeyEncoder apiKeyEncoder;

    private final OperatorAccessPolicy operatorAccessPolicy;

    private final ApiExposureProperties apiExposureProperties;

    public ApiKeyAuthenticationFilter(final UserService userService,
                                      final ApiKeyEncoder apiKeyEncoder,
                                      final OperatorAccessPolicy operatorAccessPolicy,
                                      final ApiExposureProperties apiExposureProperties) {
        this.userService = userService;
        this.apiKeyEncoder = apiKeyEncoder;
        this.operatorAccessPolicy = operatorAccessPolicy;
        this.apiExposureProperties = apiExposureProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final Optional<OrganizationUser> authenticatedUser = resolveApiKey(request)
                .map(apiKey -> this.apiKeyEncoder.decode(new ApiKey(null, apiKey)).key())
                .flatMap(this.userService::findByApiKeyHash)
                .filter(OrganizationUser::canAuthenticateWithApiKey)
                .filter(user -> this.operatorAccessPolicy.allowsAccess(user.operatorId(), user.getApiKeyIssuedAt(),
                        false));

        if (authenticatedUser.isEmpty()) {
            unauthorized(response);
            return;
        }

        final OrganizationUser user = authenticatedUser.get();
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                user.getUserId(),
                user.operatorId(),
                user.getDepartmentId(),
                null,
                user.getAuthorities()));
        SecurityContextHolder.setContext(context);
        initMdc(user, request);
        log.info("Incoming {} request (api-key)", request.getMethod());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull final HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod()) || !this.apiExposureProperties.isMcpPath(request);
    }

    private Optional<String> resolveApiKey(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authorization -> authorization.startsWith(BEARER_PREFIX))
                .map(authorization -> authorization.substring(BEARER_PREFIX.length()).trim())
                .filter(apiKey -> !apiKey.isEmpty());
    }

    private void initMdc(final OrganizationUser user, final HttpServletRequest request) {
        MDC.put("operator", user.operatorId() != null ? user.operatorId().toString() : "N/A");
        MDC.put("user", user.getUserId().value().toString());
        MDC.put("username", user.getUsername());
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private void unauthorized(final HttpServletResponse response) throws IOException {
        log.warn("API key authentication failed");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, BEARER_REALM);
        response.getWriter().write("Invalid or missing API key");
    }
}
