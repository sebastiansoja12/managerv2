package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void delete(final LocalDateTime now) {
        refreshTokenRepository.delete(now);
    }
}
