package com.warehouse.auth.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter authenticationFilter;
    private final ObjectProvider<DeviceTenantMdcFilter> deviceAuthenticationFilter;
    private final LogoutHandler logoutHandler;
    private final ApiExposureProperties apiExposureProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   @Value("${server.servlet.contextPath:}") final String contextPath) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v2/api/shipments/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/webjars/**"
                        )
                        .permitAll()
                        .requestMatchers("/v2/api/returns").authenticated()
                        .requestMatchers(apiExposureProperties.pairKeySecurityMatchers(contextPath)).permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .authenticationProvider(authenticationProvider)
                .logout(logout -> logout.logoutUrl("/v2/api/auth/logout").addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        deviceAuthenticationFilter.ifAvailable(filter ->
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class));

        return http.build();
    }
}
