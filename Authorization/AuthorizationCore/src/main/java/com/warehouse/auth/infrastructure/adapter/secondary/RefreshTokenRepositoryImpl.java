package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.TokenType;
import lombok.AllArgsConstructor;

import java.time.Instant;


@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenReadRepository repository;

    @Override
    public void save(RefreshTokenEntity refreshToken) {
        repository.save(refreshToken);
    }

    @Override
    public String save(UserEntity userEntity, String token) {
        final RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .userId(userEntity.getId())
                .tokenType(TokenType.BEARER)
                .createdDate(Instant.now())
                .expired(false)
                .revoked(false)
                .token(token)
                .build();

        return refreshToken.getId().toString();
    }
}
