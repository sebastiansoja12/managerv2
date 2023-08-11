package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.Token;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.TokenType;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.RefreshTokenNotFoundException;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RefreshTokenMapper;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenReadRepository repository;

    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public Token save(RefreshToken refreshToken) {
        final RefreshTokenEntity entity = refreshTokenMapper.map(refreshToken);
        entity.setTokenType(TokenType.BEARER);

        repository.save(entity);

        return new Token(entity.getToken());
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        return repository.findByToken(token).map(refreshTokenMapper::map)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Invalid refresh Token"));
    }
}
