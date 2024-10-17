package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;

@Repository
public interface ShipmentReadRepository extends JpaRepository<ParcelEntity, Long> {

    Optional<ParcelEntity> findParcelEntityById(Long id);

    @Query("SELECT entity FROM parcel.ParcelEntity entity where entity.id = :shipmentId")
    Optional<ParcelEntity> findShipmentById(Long shipmentId);
}
