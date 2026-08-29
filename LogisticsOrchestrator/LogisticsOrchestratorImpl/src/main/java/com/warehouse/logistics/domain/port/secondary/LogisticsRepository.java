package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.commonassets.identificator.DeliveryId;

import java.util.List;
import java.util.Optional;

public interface LogisticsRepository {
    Optional<Delivery> findById(final DeliveryId deliveryId);

    Optional<Delivery> findByTargetAndType(final DeliveryTarget target, final DeliveryType type);

    List<Delivery> findRecent(final int offset, final int limit);

    void createOrUpdate(final Delivery delivery);

    Optional<Delivery> findByShipmentId(final ShipmentId shipmentId);
}
