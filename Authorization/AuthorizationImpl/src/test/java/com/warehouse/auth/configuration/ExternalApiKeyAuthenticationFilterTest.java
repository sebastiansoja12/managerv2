package com.warehouse.auth.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.provider.ApiKeyProvider;
import com.warehouse.auth.domain.service.ApiEncoderImpl;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.commonassets.repository.OperatorDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class ExternalApiKeyAuthenticationFilterTest {

    private static final String API_KEY = "shop-api-key";
    private static final Instant ISSUED_AT = Instant.parse("2026-11-24T10:00:00Z");
    private static final UserId USER_ID = new UserId(7L);
    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);
    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);

    private final UserService userService = mock(UserService.class);
    private final OperatorAccessPolicy operatorAccessPolicy = mock(OperatorAccessPolicy.class);
    private final ApiKeyEncoder apiKeyEncoder = apiKeyEncoder();
    private final ApiExposureProperties apiExposureProperties = new ApiExposureProperties();
    private final ExternalApiKeyAuthenticationFilter filter = new ExternalApiKeyAuthenticationFilter(userService,
            operatorAccessPolicy, apiKeyEncoder, apiExposureProperties);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldFilterOnlyExternalPaths() {
        assertThat(filter.shouldNotFilter(request("/external/shipments"))).isFalse();
        assertThat(filter.shouldNotFilter(request("/external/payments/TRK-1"))).isFalse();
        assertThat(filter.shouldNotFilter(request("/externals"))).isTrue();
        assertThat(filter.shouldNotFilter(request("/shipments"))).isTrue();
    }

    @Test
    void shouldAuthenticateBusinessCustomerKeyOnExternalPath() throws Exception {
        givenUserWithKey(OrganizationUser.Role.BUSINESS_CUSTOMER);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicReference<UsernameTenantPasswordAuthenticationToken> authentication = new AtomicReference<>();
        final AtomicReference<Optional<OperatorDetails>> operatorContext = new AtomicReference<>();

        filter.doFilter(bearer(API_KEY), response, (servletRequest, servletResponse) -> {
            authentication.set((UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext()
                    .getAuthentication());
            operatorContext.set(new SecurityOperatorContextProvider().currentContext());
        });

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(authentication.get().getPrincipal()).isEqualTo(USER_ID);
        assertThat(authentication.get().getOperatorId()).isEqualTo(OPERATOR_ID);
        assertThat(authentication.get().getDepartmentId()).isEqualTo(DEPARTMENT_ID);
        assertThat(authentication.get().getCredentials()).isNull();
        assertThat(authentication.get().getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .containsExactly(ExternalApiKeyAuthenticationFilter.EXTERNAL_AUTHORITY);
        assertThat(operatorContext.get()).contains(new OperatorDetails(OPERATOR_ID, USER_ID, DEPARTMENT_ID));
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldRejectRegularUserKeyOnExternalPath() throws Exception {
        givenUserWithKey(OrganizationUser.Role.ADMIN);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean reached = new AtomicBoolean(false);

        filter.doFilter(bearer(API_KEY), response, (servletRequest, servletResponse) -> reached.set(true));

        assertUnauthorized(response, reached);
    }

    @Test
    void shouldRejectUnknownKey() throws Exception {
        when(userService.findByApiKeyHash(hash("unknown"))).thenReturn(Optional.empty());
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean reached = new AtomicBoolean(false);

        filter.doFilter(bearer("unknown"), response, (servletRequest, servletResponse) -> reached.set(true));

        assertUnauthorized(response, reached);
    }

    @Test
    void shouldRejectRequestWithoutBearerToken() throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean reached = new AtomicBoolean(false);

        filter.doFilter(request("/external/shipments"), response,
                (servletRequest, servletResponse) -> reached.set(true));

        assertUnauthorized(response, reached);
        verifyNoInteractions(userService, operatorAccessPolicy);
    }

    @Test
    void shouldRejectKeyThatWasNeverIssued() throws Exception {
        final OrganizationUser user = user(OrganizationUser.Role.BUSINESS_CUSTOMER);
        user.changeApiKey(hash(API_KEY));
        when(userService.findByApiKeyHash(hash(API_KEY))).thenReturn(Optional.of(user));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean reached = new AtomicBoolean(false);

        filter.doFilter(bearer(API_KEY), response, (servletRequest, servletResponse) -> reached.set(true));

        assertUnauthorized(response, reached);
    }

    @Test
    void shouldRejectBusinessCustomerKeyWhenOperatorAccessIsBlocked() throws Exception {
        givenUserWithKey(OrganizationUser.Role.BUSINESS_CUSTOMER);
        when(operatorAccessPolicy.allowsAccess(OPERATOR_ID, ISSUED_AT, false)).thenReturn(false);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean reached = new AtomicBoolean(false);

        filter.doFilter(bearer(API_KEY), response, (servletRequest, servletResponse) -> reached.set(true));

        assertUnauthorized(response, reached);
    }

    @Test
    void shouldRejectKeyOfDeletedBusinessCustomer() throws Exception {
        final OrganizationUser user = givenUserWithKey(OrganizationUser.Role.BUSINESS_CUSTOMER);
        user.setDeleted(true);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean reached = new AtomicBoolean(false);

        filter.doFilter(bearer(API_KEY), response, (servletRequest, servletResponse) -> reached.set(true));

        assertUnauthorized(response, reached);
    }

    private void assertUnauthorized(final MockHttpServletResponse response, final AtomicBoolean reached)
            throws Exception {
        assertThat(reached.get()).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).isEmpty();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    private OrganizationUser givenUserWithKey(final OrganizationUser.Role role) {
        final OrganizationUser user = user(role);
        user.issueApiKey(hash(API_KEY), ISSUED_AT);
        when(userService.findByApiKeyHash(hash(API_KEY))).thenReturn(Optional.of(user));
        when(operatorAccessPolicy.allowsAccess(OPERATOR_ID, ISSUED_AT, false)).thenReturn(true);
        return user;
    }

    private OrganizationUser user(final OrganizationUser.Role role) {
        final OrganizationUser user = OrganizationUser.of(USER_ID, "shop-1234567890", "password", "Sklep", "",
                "faktury@sklep.pl", role, DEPARTMENT_ID, null);
        user.assignOperator(OPERATOR_ID);
        return user;
    }

    private MockHttpServletRequest bearer(final String credential) {
        final MockHttpServletRequest request = request("/external/shipments");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + credential);
        return request;
    }

    private MockHttpServletRequest request(final String path) {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", path);
        request.setRequestURI(path);
        return request;
    }

    private String hash(final String apiKey) {
        return apiKeyEncoder.decode(new ApiKey(null, apiKey)).key();
    }

    private static ApiKeyEncoder apiKeyEncoder() {
        final ApiKeyProvider apiKeyProvider = new ApiKeyProvider();
        apiKeyProvider.setKey("test-api-key-secret-test-api-key-secret");
        return new ApiEncoderImpl(apiKeyProvider);
    }
}
