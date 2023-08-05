package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.Token;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.TokenType;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.RefreshTokenNotFoundException;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RefreshTokenMapper;
import lombok.AllArgsConstructor;

import java.time.Instant;


@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenReadRepository repository;

    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public Token save(RefreshToken refreshToken) {
        final RefreshTokenEntity entity = refreshTokenMapper.map(refreshToken);
        repository.save(entity);
        return new Token(entity.getToken());
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

    @Override
    public void validateRefreshToken(String token) {
        repository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Invalid refresh Token"));
    }
}
