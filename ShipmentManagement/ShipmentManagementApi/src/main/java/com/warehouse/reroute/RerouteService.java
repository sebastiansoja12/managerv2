package com.warehouse.reroute;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;

public interface RerouteService {
    void processRerouting(final ShipmentIdDto shipmentId);
}
