package com.warehouse.shipment.infrastructure.adapter.primary.response;

public class ShipmentResponseInformation {
    private final Status status;

    public ShipmentResponseInformation(final Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
