package com.warehouse.auth.infrastructure.adapter.secondary.filter;

import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class PlatformOwnerAuthenticationFilter extends OncePerRequestFilter {

    public static final String PLATFORM_OWNER_AUTHORITY = "ROLE_PLATFORM_OWNER";

    private static final List<String> PLATFORM_OWNER_PATHS = List.of("/platform", "/operators");

    private static final String ANNOUNCEMENT_PATH = "/announcements/active";

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort;

    private final AuthCookieService authCookieService;

    public PlatformOwnerAuthenticationFilter(final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort,
                                             final AuthCookieService authCookieService) {
        this.platformOwnerAuthenticationPort = platformOwnerAuthenticationPort;
        this.authCookieService = authCookieService;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        authCookieService.readPlatformOwnerAccessToken(request)
                .filter(accessToken -> SecurityContextHolder.getContext().getAuthentication() == null)
                .ifPresent(accessToken -> authenticate(request, accessToken));
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull final HttpServletRequest request) {
        return !isPlatformOwnerRequest(request) && authCookieService.readAccessToken(request).isPresent();
    }

    private void authenticate(final HttpServletRequest request, final String accessToken) {
        try {
            platformOwnerAuthenticationPort.authenticate(accessToken)
                    .map(PlatformOwner::getPlatformOwnerId)
                    .ifPresent(platformOwnerId -> {
                        final UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(platformOwnerId, accessToken,
                                        List.of(new SimpleGrantedAuthority(PLATFORM_OWNER_AUTHORITY)));
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        final SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authentication);
                        SecurityContextHolder.setContext(context);
                    });
        } catch (RuntimeException exception) {
            SecurityContextHolder.clearContext();
        }
    }

    private boolean isPlatformOwnerRequest(final HttpServletRequest request) {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final boolean announcementChange = path.equals(ANNOUNCEMENT_PATH)
                && !HttpMethod.GET.matches(request.getMethod());
        return announcementChange || PLATFORM_OWNER_PATHS.stream()
                .anyMatch(platformPath -> path.equals(platformPath) || path.startsWith(platformPath + "/"));
    }
}
