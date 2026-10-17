package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.PlatformOwnerRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PlatformOwnerRefreshTokenReadRepository extends JpaRepository<PlatformOwnerRefreshTokenEntity, Long> {

    Optional<PlatformOwnerRefreshTokenEntity> findByToken(final String token);

    @Transactional
    void deleteByToken(final String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM PlatformOwnerRefreshTokenEntity t WHERE t.platformOwnerId.value = :platformOwnerId")
    void deleteAllByPlatformOwnerId(@Param("platformOwnerId") final Long platformOwnerId);
}
