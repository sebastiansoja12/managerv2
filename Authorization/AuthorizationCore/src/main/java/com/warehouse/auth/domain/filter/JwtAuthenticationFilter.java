package com.warehouse.auth.domain.filter;


import java.io.IOException;

import com.warehouse.auth.domain.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

	private final RefreshTokenReadRepository refreshTokenReadRepository;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (request.getServletPath().contains("/v2/api/auth")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		username = jwtService.extractUsername(authHeader);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			final User user = this.userRepository.findByUsername(username);
			final var isTokenValid = refreshTokenReadRepository.findByToken(jwt)
					.map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
			if (jwtService.isTokenValid(jwt, user) && isTokenValid) {
				final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						user, null, null);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
