package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;

@Repository
public interface ShipmentReadRepository extends JpaRepository<ShipmentEntity, ShipmentId> {

    Optional<ShipmentEntity> findByShipmentId(final ShipmentId shipmentId);
}
