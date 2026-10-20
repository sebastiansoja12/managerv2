package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.Optional;

public interface ShipmentServicePort {

    Optional<SourceShipment> findSourceShipment(final ShipmentId shipmentId);

    Optional<ShipmentId> findDerivedShipment(final ShipmentId shipmentId);

    void markDelivered(final ShipmentId shipmentId);
}
