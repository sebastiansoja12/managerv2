package com.warehouse.auth.infrastructure.adapter.secondary.filter;

import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> DEVICE_ENDPOINTS = List.of(
            "/v2/api/deliveries",
            "/v2/api/ws"
    );

    private final JwtService jwtService;

    private final UserService userService;

    private final AuthCookieService authCookieService;

	@Override
	protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
		final String jwt = authCookieService.readAccessToken(request).orElse(null);
		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
            final String username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final User user = this.userService.findUser(username);

                if (jwtService.isTokenValid(jwt, user)) {
                    final SecurityContext context = SecurityContextHolder.createEmptyContext();
                    final UsernameTenantPasswordAuthenticationToken authToken = new UsernameTenantPasswordAuthenticationToken(
                            user.getUserId(), user.operatorId(), jwt, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
        } catch (RuntimeException exception) {
            SecurityContextHolder.clearContext();
        }
		filterChain.doFilter(request, response);
	}

    @Override
    protected boolean shouldNotFilter(@NonNull final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        return DEVICE_ENDPOINTS.stream().anyMatch(uri::startsWith);
    }
}
