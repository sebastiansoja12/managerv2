package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

/**
 * Class only for test purposes
 */
public interface RefreshTokenRepository {

    void save(RefreshTokenEntity refreshToken);

    boolean existsById(Long id);

}
