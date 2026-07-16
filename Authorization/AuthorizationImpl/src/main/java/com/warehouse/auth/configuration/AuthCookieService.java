package com.warehouse.auth.configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.warehouse.auth.domain.provider.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthCookieService {

    private final AuthProperties authProperties;
    private final JwtProvider jwtProvider;

    public AuthCookieService(final AuthProperties authProperties,
                             final JwtProvider jwtProvider) {
        this.authProperties = authProperties;
        this.jwtProvider = jwtProvider;
    }

    public Optional<String> readAccessToken(final HttpServletRequest request) {
        return readCookie(request, authProperties.getCookie().getAccessName());
    }

    public Optional<String> readRefreshToken(final HttpServletRequest request) {
        return readCookie(request, authProperties.getCookie().getRefreshName());
    }

    public void addAuthenticationCookies(final HttpServletResponse response,
                                         final String accessToken,
                                         final String refreshToken) {
        addCookie(response, buildCookie(authProperties.getCookie().getAccessName(), accessToken,
                authProperties.getCookie().getAccessPath(), jwtProvider.getExpiration()));
        addCookie(response, buildCookie(authProperties.getCookie().getRefreshName(), refreshToken,
                authProperties.getCookie().getRefreshPath(), jwtProvider.getRefreshTokenExpiration()));
    }

    public void clearAuthenticationCookies(final HttpServletResponse response) {
        addCookie(response, buildCookie(authProperties.getCookie().getAccessName(), "",
                authProperties.getCookie().getAccessPath(), 0L));
        addCookie(response, buildCookie(authProperties.getCookie().getRefreshName(), "",
                authProperties.getCookie().getRefreshPath(), 0L));
    }

    private Optional<String> readCookie(final HttpServletRequest request, final String name) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst();
    }

    private ResponseCookie buildCookie(final String name,
                                       final String value,
                                       final String path,
                                       final long maxAgeMillis) {
        final ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(authProperties.getCookie().isSecure())
                .sameSite(authProperties.getCookie().getSameSite())
                .path(path)
                .maxAge(Duration.ofMillis(maxAgeMillis));

        if (StringUtils.hasText(authProperties.getCookie().getDomain())) {
            builder.domain(authProperties.getCookie().getDomain());
        }

        return builder.build();
    }

    private void addCookie(final HttpServletResponse response, final ResponseCookie cookie) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
