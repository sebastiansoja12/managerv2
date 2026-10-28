package com.warehouse.auth.infrastructure.adapter.secondary.filter;

import com.warehouse.auth.configuration.ApiExposureProperties;
import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.configuration.AuthProperties;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterImpersonationTest {

    private static final PlatformOwnerId PLATFORM_OWNER_ID = new PlatformOwnerId(3L);

    private final UserService userService = mock(UserService.class);

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort = mock(PlatformOwnerAuthenticationPort.class);

    private final OperatorAccessPolicy operatorAccessPolicy = mock(OperatorAccessPolicy.class);

    private JwtService jwtService;

    private JwtAuthenticationFilter filter;

    private OrganizationUser superadmin;

    @BeforeEach
    void setUp() {
        final JwtProvider jwtProvider = jwtProvider();
        jwtService = new JwtServiceImpl(jwtProvider);
        filter = new JwtAuthenticationFilter(jwtService, userService,
                new AuthCookieService(new AuthProperties(), jwtProvider), platformOwnerAuthenticationPort,
                operatorAccessPolicy, new ApiExposureProperties());
        superadmin = OrganizationUser.of(new UserId(5L), "superadmin", "password", "Super", "Admin",
                "superadmin@test.pl", OrganizationUser.Role.SUPERADMIN, new DepartmentId(10L), "api-key");
        superadmin.assignOperator(OperatorId.of(20L));
        superadmin.setDeleted(false);
        when(userService.findUser("superadmin")).thenReturn(superadmin);
        when(operatorAccessPolicy.allowsAccess(eq(OperatorId.of(20L)), any(), eq(false))).thenReturn(true);
        when(operatorAccessPolicy.allowsAccess(eq(OperatorId.of(20L)), any(), eq(true))).thenReturn(true);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateImpersonatedUserAndExposeImpersonation() throws Exception {
        when(platformOwnerAuthenticationPort.isImpersonationActive(new Impersonation(PLATFORM_OWNER_ID, "owner", 77L), OperatorId.of(20L))).thenReturn(true);
        final MockHttpServletRequest request = request(impersonationToken());

        final Authentication authentication = filter(request);

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(new UserId(5L));
        assertThat(request.getAttribute(JwtAuthenticationFilter.IMPERSONATION_ATTRIBUTE))
                .isEqualTo(new Impersonation(PLATFORM_OWNER_ID, "owner", 77L));
    }

    @Test
    void shouldNotAuthenticateExternalShipmentRequest() throws Exception {
        final MockHttpServletRequest request = request(impersonationToken());
        request.setMethod("POST");
        request.setRequestURI("/v1/api/external/shipments");

        final Authentication authentication = filter(request);

        assertThat(authentication).isNull();
    }

    @Test
    void shouldRejectImpersonationWithoutActiveOwnerOrConsent() throws Exception {
        when(platformOwnerAuthenticationPort.isImpersonationActive(new Impersonation(PLATFORM_OWNER_ID, "owner", 77L), OperatorId.of(20L))).thenReturn(false);
        final MockHttpServletRequest request = request(impersonationToken());

        final Authentication authentication = filter(request);

        assertThat(authentication).isNull();
        assertThat(request.getAttribute(JwtAuthenticationFilter.IMPERSONATION_ATTRIBUTE)).isNull();
    }

    @Test
    void shouldAuthenticateRegularUserWithoutImpersonation() throws Exception {
        final MockHttpServletRequest request = request(jwtService.generateToken(superadmin));

        final Authentication authentication = filter(request);

        assertThat(authentication).isNotNull();
        assertThat(request.getAttribute(JwtAuthenticationFilter.IMPERSONATION_ATTRIBUTE)).isNull();
    }

    @Test
    void shouldRejectUserOfBlockedOperator() throws Exception {
        when(operatorAccessPolicy.allowsAccess(eq(OperatorId.of(20L)), any(), eq(false))).thenReturn(false);

        final Authentication authentication = filter(request(jwtService.generateToken(superadmin)));

        assertThat(authentication).isNull();
    }

    @Test
    void shouldPassImpersonationFlagToOperatorAccessPolicy() throws Exception {
        when(platformOwnerAuthenticationPort.isImpersonationActive(new Impersonation(PLATFORM_OWNER_ID, "owner", 77L), OperatorId.of(20L))).thenReturn(true);
        when(operatorAccessPolicy.allowsAccess(eq(OperatorId.of(20L)), any(), eq(false))).thenReturn(false);

        final Authentication authentication = filter(request(impersonationToken()));

        assertThat(authentication).isNotNull();
    }

    private String impersonationToken() {
        final PlatformOwner platformOwner = PlatformOwner.create(PLATFORM_OWNER_ID, "owner", "encoded", "Platform",
                "Owner", "owner@test.pl");
        return jwtService.generateImpersonationToken(superadmin, platformOwner, 77L, java.time.Instant.now().plusSeconds(3600));
    }

    private Authentication filter(final MockHttpServletRequest request) throws Exception {
        final Authentication[] authenticationDuringChain = new Authentication[1];
        filter.doFilter(request, new MockHttpServletResponse(),
                (servletRequest, servletResponse) ->
                        authenticationDuringChain[0] = SecurityContextHolder.getContext().getAuthentication());
        return authenticationDuringChain[0];
    }

    private MockHttpServletRequest request(final String accessToken) {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/api/users");
        request.setContextPath("/v1/api");
        request.setCookies(new Cookie("AUTH-TOKEN", accessToken));
        return request;
    }

    private static JwtProvider jwtProvider() {
        final JwtProvider jwtProvider = new JwtProvider();
        jwtProvider.setSecretKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        jwtProvider.setExpiration(86400000L);
        jwtProvider.setRefreshTokenExpiration(604800000L);
        jwtProvider.setIssuer("manager-v2");
        jwtProvider.setAudience("manager-v2-gui");
        return jwtProvider;
    }
}
