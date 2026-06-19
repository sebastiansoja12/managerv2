package com.warehouse.shipment.domain.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.model.Shipment;

public interface ShipmentRepository {

    void createOrUpdate(final Shipment shipment);

    Shipment findById(final ShipmentId shipmentId);

    boolean exists(final ShipmentId shipmentId);

    Optional<Shipment> findByExternalId(final ExternalId<String> externalId);

    Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId);

    Shipment findByTrackingNumber(final TrackingNumber trackingNumber);
}
