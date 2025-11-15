package com.warehouse.auth.configuration;

import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.port.primary.UserPortImpl;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.RolePermissionRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.service.RefreshTokenGenerator;
import com.warehouse.auth.domain.service.RefreshTokenGeneratorImpl;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.infrastructure.adapter.secondary.*;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RefreshTokenMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

    @Bean
    public UserPort userPort(final UserService userService) {
        return new UserPortImpl(userService);
    }

    @Bean
    public RolePermissionRepository rolePermissionRepository(final RolePermissionReadRepository repository) {
        return new RolePermissionRepositoryImpl(repository);
    }

    @Bean
    public RefreshTokenGenerator refreshTokenGenerator(RefreshTokenProvider refreshTokenProvider) {
        return new RefreshTokenGeneratorImpl(refreshTokenProvider);
    }

    @Bean
    public UserRepository userRepository(AuthenticationReadRepository repository) {
        return new AuthenticationRepositoryImpl(repository);
    }

    @Bean
    public RefreshTokenRepository refreshTokenRepository(RefreshTokenReadRepository repository) {
        final RefreshTokenMapper refreshTokenMapper = Mappers.getMapper(RefreshTokenMapper.class);
        return new RefreshTokenRepositoryImpl(repository, refreshTokenMapper);
    }

}
