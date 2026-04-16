package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

public class ErrorInformation {
    private final ShipmentId shipmentId;
    private final Error error;

    public ErrorInformation(final ShipmentId shipmentId, final Error error) {
        this.shipmentId = shipmentId;
        this.error = error;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public Error getError() {
        return error;
    }
}
