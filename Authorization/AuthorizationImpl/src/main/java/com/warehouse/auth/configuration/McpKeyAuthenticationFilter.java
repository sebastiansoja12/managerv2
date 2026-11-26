package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.application.port.primary.McpKeyPort;
import com.warehouse.mcpkey.application.port.primary.result.McpKeyAuthentication;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class McpKeyAuthenticationFilter extends OncePerRequestFilter {

    public static final String MCP_AUTHORITY = "MCP_ACCESS";
    public static final String BEARER_REALM = "Bearer realm=\"manager-mcp\"";

    private static final String BEARER_PREFIX = "Bearer ";

    private final McpKeyPort mcpKeyPort;
    private final McpAccessPort accessPort;

    private final UserService userService;

    private final OperatorAccessPolicy operatorAccessPolicy;

    private final ApiKeyEncoder apiKeyEncoder;

    private final ApiExposureProperties apiExposureProperties;

    public McpKeyAuthenticationFilter(final McpKeyPort mcpKeyPort,
                                      final UserService userService,
                                      final OperatorAccessPolicy operatorAccessPolicy,
                                      final ApiKeyEncoder apiKeyEncoder,
                                      final ApiExposureProperties apiExposureProperties, final McpAccessPort accessPort) {
        this.mcpKeyPort = mcpKeyPort;
        this.accessPort = accessPort;
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
        final List<GrantedAuthority> authorities = new ArrayList<>(user.getAuthorities());
        authorities.add(new SimpleGrantedAuthority(MCP_AUTHORITY));
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                user.getUserId(),
                user.operatorId(),
                user.getDepartmentId(),
                null,
                authorities));
        SecurityContextHolder.setContext(context);
        initMdc(user, request);
        log.info("Incoming {} request (mcp-key)", request.getMethod());

        try {
            if (!this.accessPort.findForUser(user.getUserId()).enabled()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "MCP access is disabled for this user");
                return;
            }
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

    private Optional<OrganizationUser> authenticate(final String credential) {
        if (McpKeySecret.hasMcpKeyPrefix(credential)) {
            return this.mcpKeyPort.authenticate(new McpKeySecret(credential))
                    .flatMap(this::resolveMcpKeyOwner);
        }
        if (this.apiExposureProperties.isMcpLegacyApiKeyEnabled()) {
            return authenticateLegacyApiKey(credential);
        }
        return Optional.empty();
    }

    private Optional<OrganizationUser> resolveMcpKeyOwner(final McpKeyAuthentication authentication) {
        return this.userService.findByIdInOperator(authentication.ownerId(), authentication.operatorId())
                .filter(user -> !user.isBusinessCustomer())
                .filter(user -> user.canAuthenticateWithMcpKey(authentication.mcpKeyId()))
                .filter(user -> this.operatorAccessPolicy.allowsAccess(authentication.operatorId(),
                        authentication.issuedAt(), false));
    }

    private Optional<OrganizationUser> authenticateLegacyApiKey(final String credential) {
        final Optional<OrganizationUser> user = this.userService.findByApiKeyHash(
                        this.apiKeyEncoder.decode(new ApiKey(null, credential)).key())
                .filter(OrganizationUser::canAuthenticateWithApiKey)
                .filter(candidate -> !candidate.isBusinessCustomer())
                .filter(candidate -> this.operatorAccessPolicy.allowsAccess(candidate.operatorId(),
                        candidate.getApiKeyIssuedAt(), false));
        user.ifPresent(candidate -> log.warn("Deprecated API key used for MCP by user {}",
                candidate.getUserId().value()));
        return user;
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

    private void unauthorized(final HttpServletResponse response) throws IOException {
        log.warn("MCP key authentication failed");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, BEARER_REALM);
        response.getWriter().write("Invalid or missing MCP key");
    }
}
