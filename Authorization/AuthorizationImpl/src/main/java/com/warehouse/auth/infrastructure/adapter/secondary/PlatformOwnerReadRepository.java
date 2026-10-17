package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.PlatformOwnerEntity;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformOwnerReadRepository extends JpaRepository<PlatformOwnerEntity, PlatformOwnerId> {

    @Query("SELECT p FROM PlatformOwnerEntity p WHERE p.username = :username AND p.deleted = false")
    Optional<PlatformOwnerEntity> findByUsername(@Param("username") final String username);

    @Query("SELECT COALESCE(MAX(p.platformOwnerId.value), 0) FROM PlatformOwnerEntity p")
    Long findMaxPlatformOwnerIdValue();

    boolean existsByUsernameOrEmail(final String username, final String email);

    long countByDeletedFalse();
}
