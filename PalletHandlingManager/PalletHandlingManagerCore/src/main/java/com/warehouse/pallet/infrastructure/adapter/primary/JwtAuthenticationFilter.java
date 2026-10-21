package com.warehouse.pallet.infrastructure.adapter.primary;

import com.warehouse.pallet.domain.vo.UserContext;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtDecodeService jwtDecodeService;

    public JwtAuthenticationFilter(final JwtDecodeService jwtDecodeService) {
        this.jwtDecodeService = jwtDecodeService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final UserContext userContext = this.jwtDecodeService.decode(
                    authorizationHeader.substring(BEARER_PREFIX.length()));
            final UsernamePasswordAuthenticationToken authentication =
                    UsernamePasswordAuthenticationToken.authenticated(userContext, null, List.of());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            MDC.put("operator", String.valueOf(userContext.operatorId().value()));
            MDC.put("user", String.valueOf(userContext.userId().value()));
        } catch (final JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("operator");
            MDC.remove("user");
        }
    }
}
