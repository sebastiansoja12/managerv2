package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RefreshToken;

import java.time.Instant;
import java.util.UUID;


public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Override
    public RefreshToken generateRefreshToken() {
        return generate();
    }


    private RefreshToken generate() {
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .createdDate(Instant.now())
                .build();
    }
}
