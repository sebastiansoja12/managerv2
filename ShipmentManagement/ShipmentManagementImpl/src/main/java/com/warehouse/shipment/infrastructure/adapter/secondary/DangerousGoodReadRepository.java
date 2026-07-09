package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.DangerousGoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DangerousGoodReadRepository extends JpaRepository<DangerousGoodEntity, ShipmentId> {
}
