package com.warehouse.auth.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.auth.infrastructure.adapter.secondary.UserReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class DepartmentReadSyncBasicAuthenticationFilter extends OncePerRequestFilter {

    private static final String ENDPOINT_PREFIX = "/departments/read-sync/";
    private static final String BASIC_PREFIX = "Basic ";

    private final UserReadRepository userReadRepository;
    private final PasswordEncoder passwordEncoder;

    public DepartmentReadSyncBasicAuthenticationFilter(final UserReadRepository userReadRepository,
                                                       final PasswordEncoder passwordEncoder) {
        this.userReadRepository = userReadRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BASIC_PREFIX)) {
            unauthorized(response);
            return;
        }

        final BasicCredentials credentials = decode(authorization);
        final UserEntity user = userReadRepository.findByUsername(credentials.username())
                .filter(foundUser -> passwordEncoder.matches(credentials.password(), foundUser.getPassword()))
                .filter(this::hasRefreshPermission)
                .orElse(null);

        if (user == null) {
            unauthorized(response);
            return;
        }

        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                user.getUserId(),
                user.operatorId(),
                null,
                user.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                        .collect(Collectors.toSet())
        ));
        SecurityContextHolder.setContext(context);

        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String contextPath = request.getContextPath();
        if (uri.startsWith(contextPath + ENDPOINT_PREFIX)) {
            return false;
        }
        return !uri.startsWith("/v2/api" + ENDPOINT_PREFIX);
    }

    private boolean hasRefreshPermission(final UserEntity user) {
        final Set<String> permissions = user.getPermissions().stream()
                .map(permission -> permission.getName())
                .collect(Collectors.toSet());
        return permissions.contains(UserPermission.ROLE_ADMIN_UPDATE.name());
    }

    private BasicCredentials decode(final String authorization) {
        final String encodedCredentials = authorization.substring(BASIC_PREFIX.length());
        final String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials), StandardCharsets.UTF_8);
        final int separator = decodedCredentials.indexOf(':');
        if (separator < 0) {
            return new BasicCredentials(decodedCredentials, "");
        }
        return new BasicCredentials(
                decodedCredentials.substring(0, separator),
                decodedCredentials.substring(separator + 1)
        );
    }

    private void unauthorized(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"department-read-sync\"");
        response.getWriter().write("Unauthorized");
    }

    private record BasicCredentials(String username, String password) {
    }
}
