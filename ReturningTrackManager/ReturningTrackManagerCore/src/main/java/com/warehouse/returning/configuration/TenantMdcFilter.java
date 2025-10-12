package com.warehouse.returning.configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.returning.domain.service.ApiKeyService;
import com.warehouse.returning.domain.vo.DecodedApiTenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantMdcFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    public TenantMdcFilter(final ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {

        MDC.put("tenant", "N/A");
        MDC.put("user", "N/A");
        MDC.put("ip", request.getRemoteAddr());
        MDC.put("uri", request.getRequestURI());
        MDC.put("class", this.getClass().getSimpleName());
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

            final String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }

            final String token = authorization.substring(7);

            try {
                final DecodedApiTenant decodedApiTenant = this.apiKeyService.decodeJwt(token);
                final String tenant = decodedApiTenant.departmentCode().value();
                final String user = decodedApiTenant.userId().value().toString();
                final String requestMethod = request.getMethod();

                MDC.put("tenant", tenant);
                MDC.put("user", user);

                log.info("Incoming {} request", requestMethod);

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
        }
    }
}
