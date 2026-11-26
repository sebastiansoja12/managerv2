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
import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.application.port.primary.McpKeyPort;
import com.warehouse.mcpkey.application.port.primary.result.McpKeyAuthentication;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

class McpKeyAuthenticationFilterTest {

    private static final McpKeySecret SECRET = new McpKeySecret("mcp_issued-mcp-key");
    private static final String LEGACY_API_KEY = "issued-api-key";
    private static final Instant ISSUED_AT = Instant.parse("2026-11-23T10:00:00Z");
    private static final UserId USER_ID = new UserId(5L);
    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);
    private static final McpKeyId MCP_KEY_ID = McpKeyId.generate();

    private final McpAccessPort accessPort = mock(McpAccessPort.class);
    private final McpKeyPort mcpKeyPort = mock(McpKeyPort.class);
    private final UserService userService = mock(UserService.class);
    private final OperatorAccessPolicy operatorAccessPolicy = mock(OperatorAccessPolicy.class);
    private final ApiKeyEncoder apiKeyEncoder = apiKeyEncoder();
    private final ApiExposureProperties apiExposureProperties = new ApiExposureProperties();
    private final McpKeyAuthenticationFilter filter = new McpKeyAuthenticationFilter(mcpKeyPort, userService,
            operatorAccessPolicy, apiKeyEncoder, apiExposureProperties, accessPort);

    @BeforeEach
    void setUp() {
        when(accessPort.findForUser(USER_ID)).thenReturn(McpAccessPolicy.unrestricted());
    }

    @Test
    void shouldRejectBlockedUserAndClearSecurityContext() throws Exception {
        givenActiveMcpKey(userWithMcpKey(MCP_KEY_ID));
        when(accessPort.findForUser(USER_ID)).thenReturn(new McpAccessPolicy(false, Map.of()));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean reached = new AtomicBoolean(false);

        filter.doFilter(bearer(SECRET.value()), response, (request, result) -> reached.set(true));

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(reached.get()).isFalse();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldFilterOnlyMcpPaths() {
        assertThat(filter.shouldNotFilter(request("/mcp"))).isFalse();
        assertThat(filter.shouldNotFilter(request("/mcp/messages"))).isFalse();
        assertThat(filter.shouldNotFilter(request("/mcpx"))).isTrue();
        assertThat(filter.shouldNotFilter(request("/shipments/1"))).isTrue();
    }

    @Test
    void shouldAuthenticateUserWithActiveMcpKey() throws Exception {
        givenActiveMcpKey(userWithMcpKey(MCP_KEY_ID));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(bearer(SECRET.value()), response, (servletRequest, servletResponse) -> {
            final UsernameTenantPasswordAuthenticationToken authentication =
                    (UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            assertThat(authentication.getPrincipal()).isEqualTo(USER_ID);
            assertThat(authentication.getOperatorId()).isEqualTo(OPERATOR_ID);
            assertThat(authentication.getDepartmentId()).isEqualTo(new DepartmentId(10L));
            assertThat(authentication.getCredentials()).isNull();
            assertThat(authentication.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                    .contains(McpKeyAuthenticationFilter.MCP_AUTHORITY);
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

        filter.doFilter(request("/mcp"), response, (servletRequest, servletResponse) -> chainCalled.set(true));

        assertThat(chainCalled.get()).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeader(HttpHeaders.WWW_AUTHENTICATE)).isEqualTo(McpKeyAuthenticationFilter.BEARER_REALM);
        verifyNoInteractions(mcpKeyPort, userService, operatorAccessPolicy);
    }

    @Test
    void shouldRejectRevokedOrExpiredMcpKey() throws Exception {
        when(mcpKeyPort.authenticate(SECRET)).thenReturn(Optional.empty());
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(bearer(SECRET.value()), response, (servletRequest, servletResponse) -> {
        });

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void shouldRejectMcpKeyNoLongerAssignedToUser() throws Exception {
        givenActiveMcpKey(userWithMcpKey(McpKeyId.generate()));
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(bearer(SECRET.value()), response, (servletRequest, servletResponse) -> {
        });

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void shouldRejectMcpKeyOfDeletedUser() throws Exception {
        final OrganizationUser user = userWithMcpKey(MCP_KEY_ID);
        user.setDeleted(true);
        givenActiveMcpKey(user);
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(bearer(SECRET.value()), response, (servletRequest, servletResponse) -> {
        });

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void shouldRejectMcpKeyWhenOperatorAccessIsBlocked() throws Exception {
        givenActiveMcpKey(userWithMcpKey(MCP_KEY_ID));
        when(operatorAccessPolicy.allowsAccess(OPERATOR_ID, ISSUED_AT, false)).thenReturn(false);
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(bearer(SECRET.value()), response, (servletRequest, servletResponse) -> {
        });

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void shouldRejectApiKeyWhenLegacyAccessIsDisabled() throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(bearer(LEGACY_API_KEY), response, (servletRequest, servletResponse) -> {
        });

        assertThat(response.getStatus()).isEqualTo(401);
        verifyNoInteractions(mcpKeyPort, userService);
    }

    @Test
    void shouldAcceptApiKeyDuringTransitionPeriod() throws Exception {
        apiExposureProperties.setMcpLegacyApiKeyEnabled(true);
        final OrganizationUser user = userWithMcpKey(null);
        user.issueApiKey(hash(LEGACY_API_KEY), ISSUED_AT);
        when(userService.findByApiKeyHash(hash(LEGACY_API_KEY))).thenReturn(Optional.of(user));
        when(operatorAccessPolicy.allowsAccess(OPERATOR_ID, ISSUED_AT, false)).thenReturn(true);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(bearer(LEGACY_API_KEY), response, (servletRequest, servletResponse) -> chainCalled.set(true));

        assertThat(chainCalled.get()).isTrue();
        verifyNoInteractions(mcpKeyPort);
    }

    @Test
    void shouldRejectBusinessCustomerApiKeyDuringTransitionPeriod() throws Exception {
        apiExposureProperties.setMcpLegacyApiKeyEnabled(true);
        final OrganizationUser user = OrganizationUser.of(USER_ID, "shop-1234567890", "password", "Sklep", "",
                "faktury@sklep.pl", OrganizationUser.Role.BUSINESS_CUSTOMER, new DepartmentId(10L), null);
        user.assignOperator(OPERATOR_ID);
        user.issueApiKey(hash(LEGACY_API_KEY), ISSUED_AT);
        when(userService.findByApiKeyHash(hash(LEGACY_API_KEY))).thenReturn(Optional.of(user));
        when(operatorAccessPolicy.allowsAccess(OPERATOR_ID, ISSUED_AT, false)).thenReturn(true);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(bearer(LEGACY_API_KEY), response, (servletRequest, servletResponse) -> chainCalled.set(true));

        assertThat(chainCalled.get()).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
    }

    private void givenActiveMcpKey(final OrganizationUser user) {
        when(mcpKeyPort.authenticate(SECRET)).thenReturn(Optional.of(
                new McpKeyAuthentication(MCP_KEY_ID, USER_ID, OPERATOR_ID, ISSUED_AT)));
        when(userService.findByIdInOperator(USER_ID, OPERATOR_ID)).thenReturn(Optional.of(user));
        when(operatorAccessPolicy.allowsAccess(OPERATOR_ID, ISSUED_AT, false)).thenReturn(true);
    }

    private OrganizationUser userWithMcpKey(final McpKeyId mcpKeyId) {
        final OrganizationUser user = OrganizationUser.of(USER_ID, "manager", "password", "Jan", "Nowak",
                "manager@test.pl", OrganizationUser.Role.MANAGER, new DepartmentId(10L), null);
        user.assignOperator(OPERATOR_ID);
        user.setMcpKeyId(mcpKeyId);
        return user;
    }

    private MockHttpServletRequest bearer(final String credential) {
        final MockHttpServletRequest request = request("/mcp");
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
