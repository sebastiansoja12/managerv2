package com.warehouse.reroute.infrastructure.adapter.primary.api;


import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;

public interface RerouteService {
    void processRerouting(final ShipmentIdDto shipmentId);
}
