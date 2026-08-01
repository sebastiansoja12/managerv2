package com.warehouse.routetracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.warehouse.routetracker.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.routetracker.infrastructure.adapter.primary.JwtAuthenticationFilter;
import com.warehouse.routetracker.infrastructure.adapter.primary.JwtDecodeService;
import com.warehouse.routetracker.infrastructure.adapter.secondary.CurrentUserServiceAdapter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration {

    @Bean
    public JwtDecodeService jwtDecodeService(final JwtProperties jwtProperties) {
        return new JwtDecodeService(jwtProperties);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(final JwtDecodeService jwtDecodeService) {
        return new JwtAuthenticationFilter(jwtDecodeService);
    }

    @Bean
    public CurrentUserServicePort currentUserServicePort() {
        return new CurrentUserServiceAdapter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, cause) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication is required")))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
