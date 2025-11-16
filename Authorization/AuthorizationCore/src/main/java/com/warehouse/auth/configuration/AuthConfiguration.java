package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.port.secondary.MailServicePort;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationRequestMapper;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationRequestMapperImpl;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationResponseMapper;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationResponseMapperImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.LoggerFactory;
import com.warehouse.auth.infrastructure.adapter.secondary.LoggerFactoryImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.MailServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.secondary.UserReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class AuthConfiguration  {

    private final LoggerFactory LOGGER_FACTORY = new LoggerFactoryImpl();

    private final UserReadRepository repository;
    

    @Bean
    public AuthenticationPort authenticationPort(final AuthenticationService authenticationService,
                                                 final UserService userService,
                                                 final JwtService jwtService,
                                                 final PasswordEncoder passwordEncoder,
                                                 final DepartmentService departmentService) {
		return new AuthenticationPortImpl(authenticationService, userService, jwtService,
				LOGGER_FACTORY.getLogger(AuthenticationPort.class), passwordEncoder, departmentService);
    }

    @Bean
    public AuthenticationService authenticationService(final RefreshTokenRepository refreshTokenRepository,
                                                       final RefreshTokenGenerator refreshTokenGenerator,
                                                       final UserRepository userRepository) {
        return new AuthenticationServiceImpl(refreshTokenRepository, refreshTokenGenerator, userRepository);
    }

    @Bean
    public JwtService jwtService(JwtProvider jwtProvider) {
        return new JwtServiceImpl(jwtProvider);
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("auth.mailServicePort")
    public MailServicePort mailServicePort(final ApplicationEventPublisher eventPublisher) {
        return new MailServiceAdapter(eventPublisher);
    }

    // request and response mappers
    @Bean(name = "authentication.requestMapper")
    public AuthenticationRequestMapper requestMapper() {
        return new AuthenticationRequestMapperImpl();
    }

    @Bean(name = "authentication.responseMapper")
    public AuthenticationResponseMapper responseMapper() {
        return new AuthenticationResponseMapperImpl();
    }

}
