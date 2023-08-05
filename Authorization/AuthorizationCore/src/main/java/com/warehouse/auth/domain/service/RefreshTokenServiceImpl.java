package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void validateRefreshToken(String token) {
        refreshTokenRepository.validateRefreshToken(token);
    }
}
