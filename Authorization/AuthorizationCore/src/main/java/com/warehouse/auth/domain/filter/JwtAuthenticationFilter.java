package com.warehouse.auth.domain.filter;


import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

	private final RefreshTokenReadRepository refreshTokenReadRepository;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		username = jwtService.extractUsername(authHeader);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			final var isTokenValid = refreshTokenReadRepository.findByToken(jwt)
					.map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
			if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
				final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
