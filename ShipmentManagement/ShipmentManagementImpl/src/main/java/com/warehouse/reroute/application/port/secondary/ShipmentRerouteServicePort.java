package com.warehouse.reroute.application.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.domain.vo.ReroutableShipment;

public interface ShipmentRerouteServicePort {

    Optional<ReroutableShipment> findShipment(final TrackingNumber trackingNumber);
}
