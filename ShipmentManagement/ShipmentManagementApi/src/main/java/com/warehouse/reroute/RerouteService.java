package com.warehouse.reroute;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;

public interface RerouteService {
    void rerouteShipment(final ShipmentIdDto shipmentId);
}
