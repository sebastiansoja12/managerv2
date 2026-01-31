package com.warehouse.shipment.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.registry.DomainContext;
import com.warehouse.shipment.domain.vo.SignatureSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;

public class Signature {

    private String signerName;
    private Instant signedAt;
    private SignatureMethod signatureMethod;
    private String documentReference;
    private ShipmentId shipmentId;
    private byte[] signature;

    public Signature() {
    }

    public Signature(final String signerName,
                     final Instant signedAt,
                     final SignatureMethod signatureMethod,
                     final String documentReference,
                     final ShipmentId shipmentId,
                     final byte[] signature) {
        this.signerName = signerName;
        this.signedAt = signedAt;
        this.signatureMethod = signatureMethod;
        this.documentReference = documentReference;
        this.shipmentId = shipmentId;
        this.signature = signature;
    }

    public Signature(final String signerName,
                     final SignatureMethod signatureMethod,
                     final String documentReference,
                     final ShipmentId shipmentId,
                     final byte[] signature) {
        this.signerName = signerName;
        this.signedAt = Instant.now();
        this.signatureMethod = signatureMethod;
        this.documentReference = documentReference;
        this.shipmentId = shipmentId;
        this.signature = signature;
        DomainContext.publish(new SignatureSigned(this.snapshot(), Instant.now()));
    }

    public static Signature from(final SignatureEntity entity) {
        return new Signature(entity.getSignerName(), entity.getSignedAt(), entity.getSignatureMethod(),
                entity.getDocumentReference(), entity.getShipmentId(), entity.getSignature());
    }
    
    public static Signature from(final SignatureChangeRequest request, final SignatureMethod signatureMethod) {
        final Signature signature;
        
        if (signatureMethod.equals(SignatureMethod.NONE)) {
            signature = new Signature();
        } else {
			signature = new Signature(request.getSignerName(), signatureMethod, request.getDocumentReference(),
					request.getShipmentId(), request.getSignature().getBytes());
        }
        return signature;
    }

    public static Signature from(final SignatureSnapshot snapshot) {
        return new Signature(snapshot.signerName(), snapshot.signedAt(), snapshot.signatureMethod(),
                snapshot.documentReference(), snapshot.shipmentId(), snapshot.signature());
    }

    public SignatureSnapshot snapshot() {
        return new SignatureSnapshot(shipmentId, signerName, documentReference, signatureMethod, signedAt, signature);
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

    public Instant getSignedAt() {
        return signedAt;
    }

    public void setSignedAt(final Instant signedAt) {
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

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(final byte[] signature) {
        this.signature = signature;
    }
}

