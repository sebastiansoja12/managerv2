package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;

import java.time.LocalDateTime;

public class Signature {

    private String signerName;
    private LocalDateTime signedAt;
    private SignatureMethod signatureMethod;
    private String documentReference;
    private ShipmentId shipmentId;

    public Signature() {
    }

    public Signature(final String signerName,
                     final LocalDateTime signedAt,
                     final SignatureMethod signatureMethod,
                     final String documentReference,
                     final ShipmentId shipmentId) {
        this.signerName = signerName;
        this.signedAt = signedAt;
        this.signatureMethod = signatureMethod;
        this.documentReference = documentReference;
        this.shipmentId = shipmentId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(final String signerName) {
        this.signerName = signerName;
    }

    public LocalDateTime getSignedAt() {
        return signedAt;
    }

    public void setSignedAt(final LocalDateTime signedAt) {
        this.signedAt = signedAt;
    }

    public SignatureMethod getSignatureMethod() {
        return signatureMethod;
    }

    public void setSignatureMethod(final SignatureMethod signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(final String documentReference) {
        this.documentReference = documentReference;
    }
}

