package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import lombok.AllArgsConstructor;


/**
 * Class only for test purposes
 */
@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenReadRepository repository;

    @Override
    public void save(RefreshTokenEntity refreshToken) {
        repository.save(refreshToken);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
