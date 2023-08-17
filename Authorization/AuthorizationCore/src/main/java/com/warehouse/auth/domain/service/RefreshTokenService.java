package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken validateRefreshToken(String token);
}
