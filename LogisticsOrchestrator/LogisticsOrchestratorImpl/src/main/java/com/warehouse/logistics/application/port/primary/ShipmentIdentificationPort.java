package com.warehouse.logistics.application.port.primary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public interface ShipmentIdentificationPort {

    Optional<TrackingNumber> findTrackingNumber(final ShipmentId shipmentId);
}
