package com.warehouse.pickuppoint.application.port.secondary;

import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;

public interface PickupPointCoordinatesServicePort {

    GeoCoordinates getCoordinates(final PickupPointAddress address);
}
