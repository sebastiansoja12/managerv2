package com.warehouse.auth.infrastructure.adapter.secondary.filter;

import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.configuration.AuthProperties;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlatformOwnerAuthenticationFilterTest {

    private static final String PLATFORM_TOKEN = "platform-token";

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort = mock(PlatformOwnerAuthenticationPort.class);

    private PlatformOwnerAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        final AuthProperties authProperties = new AuthProperties();
        filter = new PlatformOwnerAuthenticationFilter(platformOwnerAuthenticationPort,
                new AuthCookieService(authProperties, new JwtProvider()));
        when(platformOwnerAuthenticationPort.authenticate(PLATFORM_TOKEN)).thenReturn(Optional.of(
                PlatformOwner.create(new PlatformOwnerId(3L), "owner", "encoded", "Platform", "Owner", "owner@test.pl")));
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticatePlatformOwnerOnOperatorPanelPath() throws Exception {
        final Authentication authentication = filter(request("GET", "/operators/1/users", true, true));

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(new PlatformOwnerId(3L));
        assertThat(authentication.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .containsExactly(PlatformOwnerAuthenticationFilter.PLATFORM_OWNER_AUTHORITY);
    }

    @Test
    void shouldExposePlatformOwnerUsernameInRequest() throws Exception {
        final MockHttpServletRequest request = request("GET", "/operators/1/users", true, true);

        filter(request);

        assertThat(request.getAttribute(PlatformOwnerAuthenticationFilter.PLATFORM_OWNER_USERNAME_ATTRIBUTE))
                .isEqualTo("owner");
    }

    @Test
    void shouldLeaveOrganizationRequestToOrganizationTokenWhenBothCookiesArePresent() throws Exception {
        final Authentication authentication = filter(request("GET", "/users", true, true));

        assertThat(authentication).isNull();
    }

    @Test
    void shouldAuthenticatePlatformOwnerOnSharedPathWithoutOrganizationToken() throws Exception {
        final Authentication authentication = filter(request("GET", "/announcements/active", true, false));

        assertThat(authentication).isNotNull();
    }

    @Test
    void shouldPreferPlatformOwnerWhenChangingAnnouncement() throws Exception {
        final Authentication authentication = filter(request("PUT", "/announcements/active", true, true));

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(new PlatformOwnerId(3L));
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        when(platformOwnerAuthenticationPort.authenticate(anyString())).thenThrow(new IllegalArgumentException("invalid"));

        final Authentication authentication = filter(request("GET", "/operators", true, false));

        assertThat(authentication).isNull();
    }

    private Authentication filter(final MockHttpServletRequest request) throws Exception {
        final Authentication[] authenticationDuringChain = new Authentication[1];
        final MockFilterChain chain = new MockFilterChain() {
            @Override
            public void doFilter(final jakarta.servlet.ServletRequest servletRequest,
                                 final jakarta.servlet.ServletResponse servletResponse) {
                authenticationDuringChain[0] = SecurityContextHolder.getContext().getAuthentication();
            }
        };
        filter.doFilter(request, new MockHttpServletResponse(), chain);
        return authenticationDuringChain[0];
    }

    private MockHttpServletRequest request(final String method,
                                           final String path,
                                           final boolean platformCookie,
                                           final boolean organizationCookie) {
        final MockHttpServletRequest request = new MockHttpServletRequest(method, "/v1/api" + path);
        request.setContextPath("/v1/api");
        if (platformCookie && organizationCookie) {
            request.setCookies(new Cookie("PLATFORM-AUTH-TOKEN", PLATFORM_TOKEN), new Cookie("AUTH-TOKEN", "user-token"));
        } else if (platformCookie) {
            request.setCookies(new Cookie("PLATFORM-AUTH-TOKEN", PLATFORM_TOKEN));
        } else if (organizationCookie) {
            request.setCookies(new Cookie("AUTH-TOKEN", "user-token"));
        }
        return request;
    }
}
