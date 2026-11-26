package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.application.port.primary.McpKeyPort;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class McpSecurityConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain mcpSecurityFilterChain(final HttpSecurity http,
                                                      final McpKeyPort mcpKeyPort,
                                                      final UserService userService,
                                                      final OperatorAccessPolicy operatorAccessPolicy,
                                                      final ApiKeyEncoder apiKeyEncoder,
                                                      final ApiExposureProperties apiExposureProperties, final McpAccessPort accessPort)
            throws Exception {
        final McpKeyAuthenticationFilter mcpKeyAuthenticationFilter = new McpKeyAuthenticationFilter(mcpKeyPort,
                userService, operatorAccessPolicy, apiKeyEncoder, apiExposureProperties, accessPort);

        http
                .securityMatcher(apiExposureProperties.mcpSecurityMatchers())
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authenticationException) -> {
                            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, McpKeyAuthenticationFilter.BEARER_REALM);
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().hasAuthority(McpKeyAuthenticationFilter.MCP_AUTHORITY))
                .addFilterBefore(mcpKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
