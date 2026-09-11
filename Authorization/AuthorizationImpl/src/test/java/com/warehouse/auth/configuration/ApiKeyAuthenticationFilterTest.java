package com.warehouse.auth.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

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

class ApiKeyAuthenticationFilterTest {

    private static final String RAW_API_KEY = "issued-api-key";
    private static final Instant ISSUED_AT = Instant.parse("2026-10-23T10:00:00Z");

    private final UserService userService = mock(UserService.class);
    private final OperatorAccessPolicy operatorAccessPolicy = mock(OperatorAccessPolicy.class);
    private final ApiKeyEncoder apiKeyEncoder = apiKeyEncoder();
    private final ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter(userService, apiKeyEncoder,
            operatorAccessPolicy, new ApiExposureProperties());

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldFilterOnlyMcpPaths() {
        assertThat(filter.shouldNotFilter(request("/v1/api/mcp"))).isFalse();
        assertThat(filter.shouldNotFilter(request("/v1/api/mcp/messages"))).isFalse();
        assertThat(filter.shouldNotFilter(request("/v1/api/mcpx"))).isTrue();
        assertThat(filter.shouldNotFilter(request("/v1/api/shipments/1"))).isTrue();
    }

    @Test
    void shouldAuthenticateUserWithIssuedApiKey() throws Exception {
        final OrganizationUser user = userWithIssuedKey();
        when(userService.findByApiKeyHash(hash(RAW_API_KEY))).thenReturn(Optional.of(user));
        when(operatorAccessPolicy.allowsAccess(OperatorId.of(20L), ISSUED_AT, false)).thenReturn(true);
        final MockHttpServletRequest request = bearer(RAW_API_KEY);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(request, response, (servletRequest, servletResponse) -> {
            final UsernameTenantPasswordAuthenticationToken authentication =
                    (UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            assertThat(authentication.getPrincipal()).isEqualTo(new UserId(5L));
            assertThat(authentication.getOperatorId()).isEqualTo(OperatorId.of(20L));
            assertThat(authentication.getDepartmentId()).isEqualTo(new DepartmentId(10L));
            assertThat(authentication.getCredentials()).isNull();
            chainCalled.set(true);
        });

        assertThat(chainCalled.get()).isTrue();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldRejectRequestWithoutBearerToken() throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(request("/v1/api/mcp"), response, (servletRequest, servletResponse) -> chainCalled.set(true));

        assertThat(chainCalled.get()).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeader(HttpHeaders.WWW_AUTHENTICATE)).isEqualTo("Bearer realm=\"manager-mcp\"");
        verifyNoInteractions(userService, operatorAccessPolicy);
    }

    @Test
    void shouldRejectUsernameUsedAsKeyForSignupGeneratedKey() throws Exception {
        final OrganizationUser user = OrganizationUser.of(new UserId(5L), "courier", "password", "Jan", "Nowak",
                "courier@test.pl", OrganizationUser.Role.USER, new DepartmentId(10L), hash("courier"));
        user.assignOperator(OperatorId.of(20L));
        when(userService.findByApiKeyHash(hash("courier"))).thenReturn(Optional.of(user));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(bearer("courier"), response, (servletRequest, servletResponse) -> chainCalled.set(true));

        assertThat(chainCalled.get()).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void shouldRejectDeletedUser() throws Exception {
        final OrganizationUser user = userWithIssuedKey();
        user.setDeleted(true);
        when(userService.findByApiKeyHash(hash(RAW_API_KEY))).thenReturn(Optional.of(user));
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(bearer(RAW_API_KEY), response, (servletRequest, servletResponse) -> {
        });

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void shouldRejectKeyWhenOperatorAccessIsRevoked() throws Exception {
        final OrganizationUser user = userWithIssuedKey();
        when(userService.findByApiKeyHash(hash(RAW_API_KEY))).thenReturn(Optional.of(user));
        when(operatorAccessPolicy.allowsAccess(any(), any(), anyBoolean())).thenReturn(false);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(bearer(RAW_API_KEY), response, (servletRequest, servletResponse) -> chainCalled.set(true));

        assertThat(chainCalled.get()).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void shouldRejectUnknownKey() throws Exception {
        when(userService.findByApiKeyHash(hash("unknown"))).thenReturn(Optional.empty());
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(bearer("unknown"), response, (servletRequest, servletResponse) -> {
        });

        assertThat(response.getStatus()).isEqualTo(401);
    }

    private OrganizationUser userWithIssuedKey() {
        final OrganizationUser user = OrganizationUser.of(new UserId(5L), "manager", "password", "Anna", "Kowalska",
                "manager@test.pl", OrganizationUser.Role.MANAGER, new DepartmentId(10L), null);
        user.assignOperator(OperatorId.of(20L));
        user.issueApiKey(hash(RAW_API_KEY), ISSUED_AT);
        return user;
    }

    private String hash(final String rawApiKey) {
        return apiKeyEncoder.decode(new ApiKey(null, rawApiKey)).key();
    }

    private MockHttpServletRequest bearer(final String rawApiKey) {
        final MockHttpServletRequest request = request("/v1/api/mcp");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + rawApiKey);
        return request;
    }

    private MockHttpServletRequest request(final String uri) {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", uri);
        request.setRequestURI(uri);
        request.setContextPath("/v1/api");
        return request;
    }

    private static ApiKeyEncoder apiKeyEncoder() {
        final ApiKeyProvider apiKeyProvider = new ApiKeyProvider();
        apiKeyProvider.setKey("test-api-key-secret");
        return new ApiEncoderImpl(apiKeyProvider);
    }
}
