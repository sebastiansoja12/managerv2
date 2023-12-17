package com.warehouse.auth.infrastructure.adapter.secondary;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

@Repository
public interface RefreshTokenReadRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Transactional
    void deleteByToken(String token);

    @Modifying
    @Query("delete from RefreshTokenEntity t where t.expiryDate <= ?1")
    @Transactional
    void deleteAllExpiredSince(LocalDateTime now);
}
