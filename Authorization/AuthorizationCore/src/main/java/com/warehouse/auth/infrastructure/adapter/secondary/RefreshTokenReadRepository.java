package com.warehouse.auth.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

@Repository
public interface RefreshTokenReadRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Transactional
    void deleteByToken(String token);
}
