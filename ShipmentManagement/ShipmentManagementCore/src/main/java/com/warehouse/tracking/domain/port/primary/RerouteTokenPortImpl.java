package com.warehouse.tracking.domain.port.primary;


import com.warehouse.tracking.domain.model.Shipment;
import com.warehouse.tracking.domain.model.Token;

public class RerouteTokenPortImpl implements RerouteTokenPort {
    @Override
    public Token notifyShipmentRerouted(final Shipment shipment) {
        return null;
    }
}
