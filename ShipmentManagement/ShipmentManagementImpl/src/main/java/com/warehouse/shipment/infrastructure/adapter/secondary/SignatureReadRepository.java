package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;

import java.util.Optional;

public interface SignatureReadRepository extends JpaRepository<SignatureEntity, ShipmentId> {
    Optional<SignatureEntity> findByShipmentId(final ShipmentId shipmentId);
}
