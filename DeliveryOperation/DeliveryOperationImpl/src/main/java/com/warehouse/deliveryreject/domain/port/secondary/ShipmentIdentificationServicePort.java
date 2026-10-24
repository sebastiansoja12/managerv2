package com.warehouse.deliveryreject.domain.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public interface ShipmentIdentificationServicePort {

    Optional<ShipmentId> findShipmentId(final TrackingNumber trackingNumber);
}
