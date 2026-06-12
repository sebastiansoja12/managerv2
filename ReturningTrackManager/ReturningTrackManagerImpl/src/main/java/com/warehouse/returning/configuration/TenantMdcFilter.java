package com.warehouse.returning.configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.returning.domain.service.ApiKeyService;
import com.warehouse.returning.domain.vo.DecodedApiOperator;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.RestException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TenantMdcFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    private final String accessTokenCookieName;

    public TenantMdcFilter(final ApiKeyService apiKeyService,
                           @Value("${auth.cookie.access-name:AUTH-TOKEN}") final String accessTokenCookieName) {
        this.apiKeyService = apiKeyService;
        this.accessTokenCookieName = accessTokenCookieName;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {

        MDC.put("operator", "N/A");
        MDC.put("user", "N/A");
        MDC.put("username", "N/A");
        MDC.put("uri", request.getRequestURL().toString());
        MDC.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        MDC.put("method", request.getMethod());

        try {
            final String uri = request.getRequestURI();

            if (uri.startsWith("/v2/api/swagger-ui")
                    || uri.startsWith("/v2/api/v3/api-docs")
                    || uri.startsWith("/v2/api/swagger-resources")
                    || uri.startsWith("/v2/api/webjars")
                    || uri.startsWith("/v2/api/auth/login")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = resolveAccessToken(request);
            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing access token");
                return;
            }

            JwtContext.setToken(token);

            try {
                final DecodedApiOperator decodedApiOperator = this.apiKeyService.decodeJwt(token);
                final String operator = decodedApiOperator.operatorId() != null
                        ? decodedApiOperator.operatorId().toString()
                        : "N/A";
                final String user = decodedApiOperator.userId().value().toString();
                final String username = decodedApiOperator.username();
                final String requestMethod = request.getMethod();

                MDC.put("operator", operator);
                MDC.put("user", user);
                MDC.put("username", username);

                log.info("Incoming {} request", requestMethod);

            } catch (RestException e) {
                log.warn("Unauthorized request: {}", e.getMessage());
                response.setStatus(e.getCode());
                response.getWriter().write(e.getMessage());
                return;
            } catch (IllegalArgumentException e) {
                log.warn("Unauthorized request", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
                return;
            } catch (Exception e) {
                log.error("Failed to decode JWT", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Internal server error");
                return;
            }

            try {
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error("Error during request processing", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Internal server error");
            }

        } finally {
            MDC.clear();
            JwtContext.clear();
        }
    }

    private String resolveAccessToken(final HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> this.accessTokenCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(null);
    }
}
