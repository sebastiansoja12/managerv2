package com.warehouse.returning.configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.returning.domain.service.ApiKeyService;
import com.warehouse.returning.domain.vo.DecodedApiTenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TenantMdcFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    public TenantMdcFilter(final ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException {

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
            final String user = decodedApiTenant.userId().toString();
            final String clientIp = request.getRemoteAddr();
            final String requestUri = request.getRequestURI();
            final String className = this.getClass().getSimpleName();
            final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

			MDC.put("tenant", tenant);
			MDC.put("user", user);
			MDC.put("ip", clientIp);
			MDC.put("uri", requestUri);
			MDC.put("class", className);
			MDC.put("time", timestamp);

			log.info("Incoming request from user={} tenant={} uri={} ip={}", user, tenant, requestUri, clientIp);

			filterChain.doFilter(request, response);

			log.info("====Request completed successfully====");
            

        } catch (final IllegalArgumentException e) {
            log.warn("Unauthorized request: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
        } catch (final Exception e) {
            log.error("Unexpected error in filter: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
        }
    }

}

