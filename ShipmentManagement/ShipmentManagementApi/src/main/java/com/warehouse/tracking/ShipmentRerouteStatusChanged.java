package com.warehouse.tracking;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;

public class ShipmentRerouteStatusChanged extends ShipmentStatusChanged implements ShipmentStatusEvent {

    private final String token;

    public ShipmentRerouteStatusChanged(final ShipmentDto shipment, final String token) {
        super(shipment);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
