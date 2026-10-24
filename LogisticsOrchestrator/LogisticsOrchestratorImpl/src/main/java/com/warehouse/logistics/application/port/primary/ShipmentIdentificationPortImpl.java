package com.warehouse.logistics.application.port.primary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.secondary.ShipmentIdentificationServicePort;

public class ShipmentIdentificationPortImpl implements ShipmentIdentificationPort {

    private final ShipmentIdentificationServicePort shipmentIdentificationServicePort;

    public ShipmentIdentificationPortImpl(final ShipmentIdentificationServicePort shipmentIdentificationServicePort) {
        this.shipmentIdentificationServicePort = shipmentIdentificationServicePort;
    }

    @Override
    public Optional<TrackingNumber> findTrackingNumber(final ShipmentId shipmentId) {
        if (shipmentId == null || shipmentId.getValue() == null) {
            return Optional.empty();
        }
        return shipmentIdentificationServicePort.findTrackingNumber(shipmentId);
    }
}
