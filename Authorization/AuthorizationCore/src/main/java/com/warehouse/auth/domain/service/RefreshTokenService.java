package com.warehouse.auth.domain.service;

public interface RefreshTokenService {
    void validateRefreshToken(String token);
}
