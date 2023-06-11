package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.port.secondary.AuthenticationServicePort;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.infrastructure.adapter.secondary.*;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.DepotEntityMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapper;
import com.warehouse.depot.api.DepotService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@Slf4j
public class AuthConfiguration  {

    //TODO INPL-1564
    @Bean(name = "primaryAuthenticationPort")
    public AuthenticationPort authenticationPort(AuthenticationService authenticationService) {
        return new AuthenticationPortImpl(authenticationService, null);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProviderImpl();
    }

    @Bean
    public AuthenticationService authenticationService(
            AuthenticationServicePort authenticationServicePort, JwtProvider jwtProvider) {
        return new AuthenticationServiceImpl(authenticationServicePort, jwtProvider);
    }

    @Bean
    public AuthenticationServicePort authenticationServicePort(
            UserRepository userRepository, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder,
            DepotService depotService) {
        final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        final DepotEntityMapper depotEntityMapper = Mappers.getMapper(DepotEntityMapper.class);
        return new AuthenticationAdapter(userRepository, refreshTokenService, passwordEncoder, userMapper, depotService,
                depotEntityMapper);
    }

    @Bean
    public RefreshTokenService refreshTokenService() {
        return new RefreshTokenServiceImpl();
    }
    @Bean(name = "authUserRepository")
    public UserRepository userRepository(AuthenticationReadRepository repository,
        RefreshTokenReadRepository refreshTokenReadRepository) {
        final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        return new AuthenticationRepositoryImpl(repository, refreshTokenReadRepository, userMapper);
    }

    @Bean
    public RefreshTokenRepository refreshTokenRepository(RefreshTokenReadRepository repository) {
        return new RefreshTokenRepositoryImpl(repository);
    }
}
