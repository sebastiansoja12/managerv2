package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;

public class SignatureChangeRequest {
    private ShipmentId shipmentId;
    private String signature;
    private String signerName;
    private String documentReference;

    public SignatureChangeRequest(final ShipmentId shipmentId,
                                  final String signature,
                                  final String signerName,
                                  final String documentReference) {
        this.shipmentId = shipmentId;
        this.signature = signature;
        this.signerName = signerName;
        this.documentReference = documentReference;
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

    public String getDocumentReference() {
        return documentReference;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setDocumentReference(final String documentReference) {
        this.documentReference = documentReference;
    }

    public void setSignerName(final String signerName) {
        this.signerName = signerName;
    }
}
