package com.warehouse.dangerousgood.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.entity.ShipmentEntity;
import org.springframework.stereotype.Repository;

@Repository("dangerousGood.shipmentReadRepository")
public interface ShipmentReadRepository extends JpaRepository<ShipmentEntity, ShipmentId> {
}
