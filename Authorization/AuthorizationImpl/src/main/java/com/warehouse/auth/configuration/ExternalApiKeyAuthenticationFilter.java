package com.warehouse.auth.configuration;

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
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ExternalApiKeyAuthenticationFilter extends OncePerRequestFilter {

    public static final String EXTERNAL_AUTHORITY = "EXTERNAL_ACCESS";
    public static final String BEARER_REALM = "Bearer realm=\"manager-external\"";

    private static final String BEARER_PREFIX = "Bearer ";

    private final UserService userService;

    private final OperatorAccessPolicy operatorAccessPolicy;

    private final ApiKeyEncoder apiKeyEncoder;

    private final ApiExposureProperties apiExposureProperties;

    public ExternalApiKeyAuthenticationFilter(final UserService userService,
                                              final OperatorAccessPolicy operatorAccessPolicy,
                                              final ApiKeyEncoder apiKeyEncoder,
                                              final ApiExposureProperties apiExposureProperties) {
        this.userService = userService;
        this.operatorAccessPolicy = operatorAccessPolicy;
        this.apiKeyEncoder = apiKeyEncoder;
        this.apiExposureProperties = apiExposureProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final Optional<OrganizationUser> authenticatedUser = resolveCredential(request).flatMap(this::authenticate);

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
                List.of(new SimpleGrantedAuthority(EXTERNAL_AUTHORITY))));
        SecurityContextHolder.setContext(context);
        initMdc(user, request);
        log.info("Incoming {} request (external-api-key)", request.getMethod());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull final HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod()) || !this.apiExposureProperties.isExternalPath(request);
    }

    private Optional<OrganizationUser> authenticate(final String credential) {
        return this.userService.findByApiKeyHash(this.apiKeyEncoder.decode(new ApiKey(null, credential)).key())
                .filter(OrganizationUser::isBusinessCustomer)
                .filter(OrganizationUser::canAuthenticateWithApiKey)
                .filter(candidate -> this.operatorAccessPolicy.allowsAccess(candidate.operatorId(),
                        candidate.getApiKeyIssuedAt(), false));
    }

    private Optional<String> resolveCredential(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authorization -> authorization.startsWith(BEARER_PREFIX))
                .map(authorization -> authorization.substring(BEARER_PREFIX.length()).trim())
                .filter(credential -> !credential.isEmpty());
    }

    private void initMdc(final OrganizationUser user, final HttpServletRequest request) {
        MDC.put("operator", user.operatorId() != null ? user.operatorId().toString() : "N/A");
        MDC.put("user", user.getUserId().value().toString());
        MDC.put("username", user.getUsername());
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());
    }

    private void unauthorized(final HttpServletResponse response) {
        log.warn("External API key authentication failed");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, BEARER_REALM);
    }
}
