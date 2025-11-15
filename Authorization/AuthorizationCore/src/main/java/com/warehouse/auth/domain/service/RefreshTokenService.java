package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.commonassets.identificator.UserId;

import java.time.LocalDateTime;

public interface RefreshTokenService {
    RefreshToken validateRefreshToken(String token);
    void deleteRefreshToken(final UserId userId, final String token);
    RefreshToken findTokenByUserId(final UserId userId);
    void delete(LocalDateTime now);
}
