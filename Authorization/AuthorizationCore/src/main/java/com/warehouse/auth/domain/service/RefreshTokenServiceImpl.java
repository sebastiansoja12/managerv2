package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;

import com.warehouse.commonassets.identificator.UserId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken validateRefreshToken(String token) {
        return refreshTokenRepository.validateRefreshToken(token);
    }

    @Override
    public void deleteRefreshToken(final UserId userId, final String token) {
        refreshTokenRepository.delete(token);
    }

    @Override
    public RefreshToken findTokenByUserId(final UserId userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}
