package com.warehouse.reroute;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;

public interface RerouteService {
    void rerouteShipment(final ShipmentDto shipmentDto);
}
