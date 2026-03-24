package com.warehouse.auth.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.UserApiService;
import com.warehouse.auth.domain.port.primary.*;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.RolePermissionRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.infrastructure.adapter.primary.CurrentUserApiServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.primary.UserApiServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.secondary.*;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RefreshTokenMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

@Configuration
public class UserConfiguration {

    @Bean
    public UserPort userPort(final UserService userService, final AuthenticationService authenticationService) {
        return new UserPortImpl(userService, authenticationService);
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
    public UserRepository userRepository(final OperatorFilteredRepository<UserEntity> repository) {
        return new UserRepositoryImpl(repository);
    }

    @Bean
    public RefreshTokenRepository refreshTokenRepository(RefreshTokenReadRepository repository) {
        final RefreshTokenMapper refreshTokenMapper = Mappers.getMapper(RefreshTokenMapper.class);
        return new RefreshTokenRepositoryImpl(repository, refreshTokenMapper);
    }

    @Bean
    public RefreshTokenService refreshTokenService(final RefreshTokenRepository refreshTokenRepository) {
        return new RefreshTokenServiceImpl(refreshTokenRepository);
    }

    @Bean
    public RefreshTokenPortObserverPort refreshTokenPortObserverPort(final RefreshTokenService refreshTokenService) {
        return new RefreshTokenPortObserverPortImpl(refreshTokenService);
    }

    @Bean
    public CurrentUserAuthenticationService currentUserAuthenticationService(final AuthenticationService authenticationService,
                                                                             final UserPort userPort,
                                                                             final JwtService jwtService) {
        return new CurrentUserAuthenticationService(authenticationService, userPort, jwtService);
    }

    @Bean
    public CurrentUserAuthenticationPort currentUserAuthenticationPort(
            final CurrentUserAuthenticationService currentUserAuthenticationService) {
        return new CurrentUserAuthenticationPortImpl(currentUserAuthenticationService);
    }

    @Bean
    public CurrentUserApiService currentUserService(final CurrentUserAuthenticationPort currentUserAuthenticationPort) {
        return new CurrentUserApiServiceAdapter(currentUserAuthenticationPort);
    }

    @Bean
    public UserApiService userApiService(final UserService userService) {
        return new UserApiServiceAdapter(userService);
    }

}
