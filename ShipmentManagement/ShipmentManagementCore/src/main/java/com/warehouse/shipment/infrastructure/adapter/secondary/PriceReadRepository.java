package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.PriceEntity;

public interface PriceReadRepository extends JpaRepository<PriceEntity, Long> {
    Optional<PriceEntity> findByShipmentSize(final ShipmentSize shipmentSize);
}
