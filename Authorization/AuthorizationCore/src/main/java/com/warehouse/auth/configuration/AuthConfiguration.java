package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.AuthenticationServiceImpl;
import com.warehouse.auth.domain.service.RefreshTokenService;
import com.warehouse.auth.domain.service.RefreshTokenServiceImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationRepositoryImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenRepositoryImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapper;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class AuthConfiguration  {

    @Bean
    public AuthenticationPort authenticationPort(AuthenticationService authenticationService,
	    AuthenticationManager authenticationManager) {
        return new AuthenticationPortImpl(authenticationService, authenticationManager);
    }

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationServiceImpl();
    }
    @Bean
    public RefreshTokenService refreshTokenService() {
        return new RefreshTokenServiceImpl();
    }

	@Bean
	public UserRepository userRepository(AuthenticationReadRepository repository,
			RefreshTokenReadRepository refreshTokenReadRepository) {
		final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
		return new AuthenticationRepositoryImpl(repository, refreshTokenReadRepository, userMapper);
	}

    @Bean
    public RefreshTokenRepository refreshTokenRepository(RefreshTokenReadRepository repository) {
        return new RefreshTokenRepositoryImpl(repository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				return authentication;
			}

			@Override
			public boolean supports(Class<?> authentication) {
				return authentication.isAnnotation();
			}
		};
	}

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            try {
                request.logout();
                authentication.setAuthenticated(false);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
