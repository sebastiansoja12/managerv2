package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken validateRefreshToken(String token) {
        return refreshTokenRepository.validateRefreshToken(token);
    }
}
