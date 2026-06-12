package com.warehouse.shipment.domain.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ShipmentReadModelRepository {

    void sync(final ShipmentSnapshot snapshot);

    Optional<Shipment> findById(final ShipmentId shipmentId);

    boolean exists(final ShipmentId shipmentId);

    Optional<Shipment> findByExternalId(final ExternalId<String> externalId);

    Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId);

    Optional<Shipment> findByTrackingNumber(final TrackingNumber trackingNumber);
}
