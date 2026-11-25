package com.warehouse.auth.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.auth.domain.provider.ApiKeyProvider;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.ApiEncoderImpl;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.DepartmentService;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.LoginThrottle;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.auth.infrastructure.adapter.secondary.UserReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.PlatformOwnerAuthenticationFilter;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringJUnitWebConfig(ExternalSecurityConfigurationTest.TestSecurityConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ExternalSecurityConfigurationTest {

    private static final String SHOP_API_KEY = "shop-api-key";
    private static final String ADMIN_API_KEY = "admin-api-key";
    private static final String SHOP_JWT = "shop-jwt";
    private static final String ADMIN_JWT = "admin-jwt";
    private static final Instant ISSUED_AT = Instant.parse("2026-11-24T10:00:00Z");
    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private final Filter springSecurityFilterChain;
    private final UserService userService;
    private final OperatorAccessPolicy operatorAccessPolicy;
    private final JwtService jwtService;
    private final ApiKeyEncoder apiKeyEncoder;

    ExternalSecurityConfigurationTest(@Qualifier("springSecurityFilterChain") final Filter springSecurityFilterChain,
                                      final UserService userService,
                                      final OperatorAccessPolicy operatorAccessPolicy,
                                      final JwtService jwtService,
                                      final ApiKeyEncoder apiKeyEncoder) {
        this.springSecurityFilterChain = springSecurityFilterChain;
        this.userService = userService;
        this.operatorAccessPolicy = operatorAccessPolicy;
        this.jwtService = jwtService;
        this.apiKeyEncoder = apiKeyEncoder;
    }

    @BeforeEach
    void setUp() {
        reset(userService, operatorAccessPolicy, jwtService);
        final OrganizationUser shop = user(new UserId(7L), "shop-1234567890",
                OrganizationUser.Role.BUSINESS_CUSTOMER, SHOP_API_KEY);
        final OrganizationUser admin = user(new UserId(8L), "admin", OrganizationUser.Role.ADMIN, ADMIN_API_KEY);
        when(userService.findByApiKeyHash(hash(SHOP_API_KEY))).thenReturn(Optional.of(shop));
        when(userService.findByApiKeyHash(hash(ADMIN_API_KEY))).thenReturn(Optional.of(admin));
        when(operatorAccessPolicy.allowsAccess(any(), any(), anyBoolean())).thenReturn(true);
        givenJwt(SHOP_JWT, shop);
        givenJwt(ADMIN_JWT, admin);
    }

    @Test
    void shouldAuthenticateBusinessCustomerKeyOnExternalPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(withApiKey(request("POST", "/external/shipments"),
                SHOP_API_KEY), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(reached.get()).isTrue();
    }

    @Test
    void shouldAuthenticateBusinessCustomerKeyOnExternalPaymentPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(withApiKey(request("GET", "/external/payments/TRK-1"),
                SHOP_API_KEY), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(reached.get()).isTrue();
    }

    @Test
    void shouldRejectBusinessCustomerKeyOnInternalPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(withApiKey(request("GET", "/shipments"), SHOP_API_KEY),
                reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    @Test
    void shouldRejectBusinessCustomerJwtOnInternalPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(withJwt(request("GET", "/shipments"), SHOP_JWT), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    @Test
    void shouldAuthenticateRegularUserJwtOnInternalPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(withJwt(request("GET", "/shipments"), ADMIN_JWT), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(reached.get()).isTrue();
    }

    @Test
    void shouldRejectRegularUserKeyOnExternalPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(withApiKey(request("POST", "/external/shipments"),
                ADMIN_API_KEY), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    @Test
    void shouldRejectRegularUserJwtOnExternalPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(withJwt(request("GET", "/external/payments/TRK-1"),
                ADMIN_JWT), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    @Test
    void shouldRejectAnonymousRequestOnExternalPath() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(request("POST", "/external/shipments"), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    @Test
    void shouldKeepStripeWebhookPublic() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform(request("POST", "/billing/webhooks/stripe"), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(reached.get()).isTrue();
    }

    private MockHttpServletResponse perform(final MockHttpServletRequest request, final AtomicBoolean reached)
            throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        springSecurityFilterChain.doFilter(request, response, (servletRequest, servletResponse) -> reached.set(true));
        return response;
    }

    private MockHttpServletRequest request(final String method, final String path) {
        final MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        request.setRequestURI(path);
        return request;
    }

    private MockHttpServletRequest withApiKey(final MockHttpServletRequest request, final String apiKey) {
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
        return request;
    }

    private MockHttpServletRequest withJwt(final MockHttpServletRequest request, final String jwt) {
        request.setCookies(new Cookie("AUTH-TOKEN", jwt));
        return request;
    }

    private void givenJwt(final String jwt, final OrganizationUser user) {
        when(jwtService.extractUsername(jwt)).thenReturn(user.getUsername());
        when(userService.findUser(user.getUsername())).thenReturn(user);
        when(jwtService.extractImpersonation(jwt)).thenReturn(Optional.empty());
        when(jwtService.isTokenValid(jwt, user)).thenReturn(true);
        when(jwtService.extractIssuedAt(jwt)).thenReturn(Optional.of(ISSUED_AT));
        when(jwtService.extractDepartmentId(jwt)).thenReturn(Optional.empty());
    }

    private OrganizationUser user(final UserId userId, final String username, final OrganizationUser.Role role,
                                  final String apiKey) {
        final OrganizationUser user = OrganizationUser.of(userId, username, "password", "Jan", "Nowak",
                username + "@test.pl", role, new DepartmentId(10L), null);
        user.assignOperator(OPERATOR_ID);
        user.issueApiKey(hash(apiKey), ISSUED_AT);
        return user;
    }

    private String hash(final String apiKey) {
        return apiKeyEncoder.decode(new ApiKey(null, apiKey)).key();
    }

    @Configuration
    @Import({SecurityConfiguration.class, ExternalSecurityConfiguration.class})
    static class TestSecurityConfiguration {

        @Bean
        UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        OperatorAccessPolicy operatorAccessPolicy() {
            return mock(OperatorAccessPolicy.class);
        }

        @Bean
        JwtService jwtService() {
            return mock(JwtService.class);
        }

        @Bean
        ApiKeyEncoder apiKeyEncoder() {
            final ApiKeyProvider apiKeyProvider = new ApiKeyProvider();
            apiKeyProvider.setKey("test-api-key-secret-test-api-key-secret");
            return new ApiEncoderImpl(apiKeyProvider);
        }

        @Bean
        ApiExposureProperties apiExposureProperties() {
            return new ApiExposureProperties();
        }

        @Bean
        AuthProperties authProperties() {
            return new AuthProperties();
        }

        @Bean
        AuthCookieService authCookieService(final AuthProperties authProperties) {
            return new AuthCookieService(authProperties, new JwtProvider());
        }

        @Bean
        AuthenticationProvider authenticationProvider() {
            return mock(AuthenticationProvider.class);
        }

        @Bean
        UserAuthenticationEntryPoint userAuthenticationEntryPoint() {
            return new UserAuthenticationEntryPoint();
        }

        @Bean
        JwtAuthenticationFilter jwtAuthenticationFilter(final JwtService jwtService,
                                                        final UserService userService,
                                                        final AuthCookieService authCookieService,
                                                        final OperatorAccessPolicy operatorAccessPolicy,
                                                        final ApiExposureProperties apiExposureProperties) {
            return new JwtAuthenticationFilter(jwtService, userService, authCookieService,
                    mock(PlatformOwnerAuthenticationPort.class), operatorAccessPolicy, apiExposureProperties,
                    new DepartmentService(mock(DepartmentServicePort.class)));
        }

        @Bean
        PlatformOwnerAuthenticationFilter platformOwnerAuthenticationFilter(
                final AuthCookieService authCookieService,
                final ApiExposureProperties apiExposureProperties) {
            return new PlatformOwnerAuthenticationFilter(mock(PlatformOwnerAuthenticationPort.class),
                    authCookieService, apiExposureProperties);
        }

        @Bean
        DepartmentReadSyncBasicAuthenticationFilter departmentReadSyncBasicAuthenticationFilter() {
            return new DepartmentReadSyncBasicAuthenticationFilter(mock(UserReadRepository.class),
                    mock(PasswordEncoder.class), mock(LoginThrottle.class));
        }

        @Bean
        ShipmentReadSyncBasicAuthenticationFilter shipmentReadSyncBasicAuthenticationFilter() {
            return new ShipmentReadSyncBasicAuthenticationFilter(mock(UserReadRepository.class),
                    mock(PasswordEncoder.class), mock(LoginThrottle.class));
        }
    }
}
