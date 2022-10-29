package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.infrastructure.adapter.entity.RerouteTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;



@Repository
public interface RerouteTokenReadRepository extends JpaRepository<RerouteTokenEntity, Long> {

    @Query("SELECT entity FROM reroute.RerouteTokenEntity entity" +
           " WHERE entity.parcelId=:parcelId and entity.token=:token")
    Optional<RerouteTokenEntity> loadByTokenAndParcelId(
       @Param("token") Integer token,
       @Param("parcelId") Long parcelId
    );

    Optional<RerouteTokenEntity> findByToken(Integer token);

    @Transactional
    void deleteByToken(Integer token);

    @Modifying
    @Query("delete from reroute.RerouteTokenEntity t where t.expiryDate <= ?1")
    @Transactional
    void deleteAllExpiredSince(Instant now);
}
