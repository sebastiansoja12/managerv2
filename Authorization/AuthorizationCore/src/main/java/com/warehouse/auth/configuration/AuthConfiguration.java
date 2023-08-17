package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.infrastructure.adapter.secondary.*;
import org.mapstruct.factory.Mappers;
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

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationRequestMapper;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationRequestMapperImpl;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationResponseMapper;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationResponseMapperImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RefreshTokenMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapper;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class AuthConfiguration  {

    private final AuthenticationReadRepository repository;

    private final LoggerFactory LOGGER_FACTORY = new LoggerFactoryImpl();
    
	@Bean
	public AuthenticationPort authenticationPort(AuthenticationService authenticationService,
			AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService) {
		final String loggerName = "Authentication";
		return new AuthenticationPortImpl(authenticationService, passwordEncoder, authenticationManager, jwtService,
				LOGGER_FACTORY.getLogger(loggerName));
	}

    @Bean
    public JwtService jwtService(JwtProvider jwtProvider) {
        return new JwtServiceImpl(jwtProvider);
    }

    @Bean
    public RefreshTokenGenerator refreshTokenGenerator(RefreshTokenProvider refreshTokenProvider) {
        return new RefreshTokenGeneratorImpl(refreshTokenProvider);
    }

	@Bean
	public UserRepository userRepository(AuthenticationReadRepository repository) {
		final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
		return new AuthenticationRepositoryImpl(repository, userMapper);
	}

    @Bean
    public RefreshTokenRepository refreshTokenRepository(RefreshTokenReadRepository repository) {
        final RefreshTokenMapper refreshTokenMapper = Mappers.getMapper(RefreshTokenMapper.class);
        return new RefreshTokenRepositoryImpl(repository, refreshTokenMapper);
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
