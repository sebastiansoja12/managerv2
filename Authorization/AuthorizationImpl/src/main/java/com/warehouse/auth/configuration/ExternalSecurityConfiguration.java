package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.UserService;
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
public class ExternalSecurityConfiguration {

    @Bean
    @Order(2)
    public SecurityFilterChain externalSecurityFilterChain(final HttpSecurity http,
                                                           final UserService userService,
                                                           final OperatorAccessPolicy operatorAccessPolicy,
                                                           final ApiKeyEncoder apiKeyEncoder,
                                                           final ApiExposureProperties apiExposureProperties)
            throws Exception {
        final ExternalApiKeyAuthenticationFilter externalApiKeyAuthenticationFilter =
                new ExternalApiKeyAuthenticationFilter(userService, operatorAccessPolicy, apiKeyEncoder,
                        apiExposureProperties);

        http
                .securityMatcher(apiExposureProperties.externalSecurityMatchers())
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authenticationException) -> {
                            response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                                    ExternalApiKeyAuthenticationFilter.BEARER_REALM);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().hasAuthority(ExternalApiKeyAuthenticationFilter.EXTERNAL_AUTHORITY))
                .addFilterBefore(externalApiKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
