package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public interface RefreshTokenRepository {

    void save(RefreshTokenEntity refreshToken);

    String save(UserEntity userEntity, String token);
}
