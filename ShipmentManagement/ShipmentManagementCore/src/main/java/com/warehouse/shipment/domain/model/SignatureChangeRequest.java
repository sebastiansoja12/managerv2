package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;

public class SignatureChangeRequest {
    private ShipmentId shipmentId;
    private String signature;

    public SignatureChangeRequest(final ShipmentId shipmentId,
                                  final String signature) {
        this.shipmentId = shipmentId;
        this.signature = signature;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(final String signature) {
        this.signature = signature;
    }
}
