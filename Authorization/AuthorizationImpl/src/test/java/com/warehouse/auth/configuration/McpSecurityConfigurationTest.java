package com.warehouse.auth.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.application.port.primary.McpKeyPort;
import com.warehouse.mcpkey.application.port.primary.result.McpKeyAuthentication;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringJUnitWebConfig(McpSecurityConfigurationTest.TestSecurityConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class McpSecurityConfigurationTest {

    private static final McpKeySecret SECRET = new McpKeySecret("mcp_integration-test-key");
    private static final UserId USER_ID = new UserId(5L);
    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);
    private static final McpKeyId MCP_KEY_ID = McpKeyId.generate();
    private static final Instant ISSUED_AT = Instant.parse("2026-11-23T10:00:00Z");

    private final Filter springSecurityFilterChain;
    private final McpKeyPort mcpKeyPort;
    private final UserService userService;
    private final OperatorAccessPolicy operatorAccessPolicy;

    McpSecurityConfigurationTest(@Qualifier("springSecurityFilterChain") final Filter springSecurityFilterChain,
                                 final McpKeyPort mcpKeyPort,
                                 final UserService userService,
                                 final OperatorAccessPolicy operatorAccessPolicy) {
        this.springSecurityFilterChain = springSecurityFilterChain;
        this.mcpKeyPort = mcpKeyPort;
        this.userService = userService;
        this.operatorAccessPolicy = operatorAccessPolicy;
    }

    @BeforeEach
    void setUp() {
        reset(mcpKeyPort, userService, operatorAccessPolicy);
        final OrganizationUser user = OrganizationUser.of(USER_ID, "manager", "password", "Jan", "Nowak",
                "manager@test.pl", OrganizationUser.Role.MANAGER, new DepartmentId(10L), null);
        user.assignOperator(OPERATOR_ID);
        user.setMcpKeyId(MCP_KEY_ID);
        when(mcpKeyPort.authenticate(SECRET)).thenReturn(Optional.of(
                new McpKeyAuthentication(MCP_KEY_ID, USER_ID, OPERATOR_ID, ISSUED_AT)));
        when(userService.findByIdInOperator(USER_ID, OPERATOR_ID)).thenReturn(Optional.of(user));
        when(operatorAccessPolicy.allowsAccess(OPERATOR_ID, ISSUED_AT, false)).thenReturn(true);
    }

    @Test
    void shouldGrantMcpAccessWithMcpKey() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform("/mcp", SECRET.value(), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(reached.get()).isTrue();
    }

    @Test
    void shouldRejectApiKeyOnMcpEndpoint() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform("/mcp", "legacy-api-key", reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    @Test
    void shouldRejectRevokedMcpKeyOnMcpEndpoint() throws Exception {
        when(mcpKeyPort.authenticate(SECRET)).thenReturn(Optional.empty());
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform("/mcp", SECRET.value(), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    @Test
    void shouldRejectMcpKeyOnRestEndpoint() throws Exception {
        final AtomicBoolean reached = new AtomicBoolean(false);

        final MockHttpServletResponse response = perform("/shipments/1", SECRET.value(), reached);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(reached.get()).isFalse();
    }

    private MockHttpServletResponse perform(final String path, final String credential, final AtomicBoolean reached)
            throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", path);
        request.setRequestURI(path);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + credential);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        springSecurityFilterChain.doFilter(request, response, (servletRequest, servletResponse) -> reached.set(true));
        return response;
    }

    @Configuration
    @EnableWebSecurity
    @Import(McpSecurityConfiguration.class)
    static class TestSecurityConfiguration {

        @Bean
        McpAccessPort accessPort() {
            final McpAccessPort port = mock(McpAccessPort.class);
            when(port.findForUser(USER_ID)).thenReturn(McpAccessPolicy.unrestricted());
            return port;
        }

        @Bean
        McpKeyPort mcpKeyPort() {
            return mock(McpKeyPort.class);
        }

        @Bean
        UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        OperatorAccessPolicy operatorAccessPolicy() {
            return mock(OperatorAccessPolicy.class);
        }

        @Bean
        ApiKeyEncoder apiKeyEncoder() {
            return mock(ApiKeyEncoder.class);
        }

        @Bean
        ApiExposureProperties apiExposureProperties() {
            return new ApiExposureProperties();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", new CorsConfiguration());
            return source;
        }

        @Bean
        @Order(2)
        SecurityFilterChain restSecurityFilterChain(final HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exception -> exception.authenticationEntryPoint(
                            (request, response, authenticationException) ->
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
            return http.build();
        }
    }
}
