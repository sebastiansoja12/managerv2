package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.Token;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public interface RefreshTokenRepository {

    Token save(RefreshToken refreshToken);

    String save(UserEntity userEntity, String token);

    void validateRefreshToken(String token);
}
