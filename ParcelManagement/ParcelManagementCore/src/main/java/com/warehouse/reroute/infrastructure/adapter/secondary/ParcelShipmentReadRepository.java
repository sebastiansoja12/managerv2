package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParcelShipmentReadRepository extends JpaRepository<ParcelEntity, Long> {

    @Query("SELECT entity FROM reroute.ParcelEntity entity" +
            " WHERE entity.id=:parcelId")
    Optional<ParcelEntity> loadByParcelId(@Param("parcelId") Long parcelId);
}
