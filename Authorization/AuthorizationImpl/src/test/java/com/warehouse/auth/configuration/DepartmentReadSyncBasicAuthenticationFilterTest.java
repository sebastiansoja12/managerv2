package com.warehouse.auth.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.infrastructure.adapter.secondary.UserReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RolePermissionEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

class DepartmentReadSyncBasicAuthenticationFilterTest {

    private final UserReadRepository userReadRepository = mock(UserReadRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final DepartmentReadSyncBasicAuthenticationFilter filter =
            new DepartmentReadSyncBasicAuthenticationFilter(userReadRepository, passwordEncoder);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldFilterDepartmentReadSyncEndpointWithApiPrefix() {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST",
                "/v2/api/departments/read-sync/1");
        request.setRequestURI("/v2/api/departments/read-sync/1");

        assertFalse(filter.shouldNotFilter(request));
    }

    @Test
    void shouldAuthenticateDepartmentReadSyncRequestWithBasicAuth() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST",
                "/v2/api/departments/read-sync/1");
        request.setRequestURI("/v2/api/departments/read-sync/1");
        request.addHeader(HttpHeaders.AUTHORIZATION, basic("admin", "secret"));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final UserEntity user = user();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        when(userReadRepository.findByUsername("admin")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("secret", "encoded-secret")).thenReturn(true);

        filter.doFilter(request, response, (servletRequest, servletResponse) -> {
            final var authentication = SecurityContextHolder.getContext().getAuthentication();
            assertThat(authentication).isInstanceOf(UsernameTenantPasswordAuthenticationToken.class);
            final var tenantAuthentication = (UsernameTenantPasswordAuthenticationToken) authentication;
            assertThat(tenantAuthentication.getPrincipal()).isEqualTo(new UserId(11L));
            assertThat(tenantAuthentication.getOperatorId()).isEqualTo(OperatorId.of(10001L));
            assertThat(tenantAuthentication.getAuthorities())
                    .extracting("authority")
                    .containsExactly("ROLE_ADMIN_UPDATE");
            chainCalled.set(true);
        });

        assertThat(chainCalled.get()).isTrue();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldRejectRequestWithoutBasicAuth() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST",
                "/v2/api/departments/read-sync/1");
        request.setRequestURI("/v2/api/departments/read-sync/1");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean chainCalled = new AtomicBoolean(false);

        filter.doFilter(request, response, (servletRequest, servletResponse) -> chainCalled.set(true));

        assertThat(chainCalled.get()).isFalse();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeader(HttpHeaders.WWW_AUTHENTICATE)).isEqualTo("Basic realm=\"department-read-sync\"");
        verifyNoInteractions(userReadRepository, passwordEncoder);
    }

    private String basic(final String username, final String password) {
        final String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    private UserEntity user() {
        final UserEntity user = UserEntity.builder()
                .userId(new UserId(11L))
                .username("admin")
                .password("encoded-secret")
                .firstName("Admin")
                .lastName("User")
                .email("admin@test.pl")
                .role(UserEntity.Role.ADMIN)
                .departmentCode(new DepartmentCode("TST"))
                .language("PL")
                .apiKey("api-key")
                .permissions(Set.of(new RolePermissionEntity(1L, "ROLE_ADMIN_UPDATE")))
                .deleted(false)
                .initial(false)
                .build();
        user.assignOperator(OperatorId.of(10001L));
        return user;
    }
}
