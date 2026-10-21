package com.warehouse.reroute.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;

public interface RerouteTokenJpaRepository extends JpaRepository<RerouteTokenEntity, Long> {

    Optional<RerouteTokenEntity> findFirstByShipmentIdOrderByCreatedAtDescIdDesc(final Long shipmentId);

    List<RerouteTokenEntity> findAllByShipmentIdAndUsedAtIsNullAndExpiresAtAfter(final Long shipmentId,
                                                                                 final Instant now);

    @Modifying
    @Transactional
    @Query("delete from reroute.RerouteTokenEntity token where token.expiresAt < :expiredBefore")
    void deleteAllExpiredBefore(@Param("expiredBefore") final Instant expiredBefore);
}
