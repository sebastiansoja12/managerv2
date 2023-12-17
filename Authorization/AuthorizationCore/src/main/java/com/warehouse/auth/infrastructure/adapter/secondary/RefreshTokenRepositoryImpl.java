package com.warehouse.auth.infrastructure.adapter.secondary;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.vo.Token;
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
        entity.setCreatedDate(LocalDateTime.now());
        entity.setExpiryDate(LocalDateTime.now().plus(ChronoUnit.HALF_DAYS.getDuration()));
        entity.setTokenType(TokenType.BEARER);
        repository.save(entity);

        return refreshTokenMapper.mapToToken(entity);
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        return repository.findByToken(token).map(refreshTokenMapper::map)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Invalid refresh Token"));
    }

    @Override
    public void delete(String refreshToken) {
        repository.deleteByToken(refreshToken);
    }

    @Override
    public void delete(LocalDateTime time) {
        repository.deleteAllExpiredSince(time);
    }
}
