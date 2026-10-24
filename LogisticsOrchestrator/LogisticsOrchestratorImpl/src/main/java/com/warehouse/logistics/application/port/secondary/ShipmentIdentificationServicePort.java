package com.warehouse.logistics.application.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public interface ShipmentIdentificationServicePort {

    Optional<TrackingNumber> findTrackingNumber(final ShipmentId shipmentId);
}
