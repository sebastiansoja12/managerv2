package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Optional;

public interface ShipmentRepository {

    void createOrUpdate(final Shipment shipment);

    Shipment findById(final ShipmentId shipmentId);

    boolean exists(final ShipmentId shipmentId);

    Optional<Shipment> findByExternalId(final ExternalId<String> externalId);

    Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId);
}
