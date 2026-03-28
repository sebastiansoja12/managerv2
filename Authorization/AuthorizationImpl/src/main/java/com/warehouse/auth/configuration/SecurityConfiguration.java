package com.warehouse.auth.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    private final ApiExposureProperties apiExposureProperties;
    private final UserAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthProperties authProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   @Value("${server.servlet.contextPath:}") final String contextPath,
                                                   final CookieCsrfTokenRepository csrfTokenRepository) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/login", "/auth/refresh", "/auth/logout", "/auth/csrf").permitAll()
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
                .authenticationProvider(authenticationProvider);

        deviceAuthenticationFilter.ifAvailable(filter ->
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class));

        return http.build();
    }

    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        final CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieName("XSRF-TOKEN");
        csrfTokenRepository.setHeaderName("X-XSRF-TOKEN");
        csrfTokenRepository.setCookiePath("/");
        csrfTokenRepository.setCookieCustomizer(cookieBuilder -> {
            cookieBuilder.secure(authProperties.getCookie().isSecure())
                    .sameSite(authProperties.getCookie().getSameSite());
            if (StringUtils.hasText(authProperties.getCookie().getDomain())) {
                cookieBuilder.domain(authProperties.getCookie().getDomain());
            }
        });
        return csrfTokenRepository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(authProperties.getCors().getAllowedOrigins());
        configuration.setAllowedMethods(authProperties.getCors().getAllowedMethods());
        configuration.setAllowedHeaders(authProperties.getCors().getAllowedHeaders());
        configuration.setExposedHeaders(authProperties.getCors().getExposedHeaders());
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(authProperties.getCors().getMaxAgeSeconds());

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
