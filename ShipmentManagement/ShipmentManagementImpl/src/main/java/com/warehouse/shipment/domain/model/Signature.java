package com.warehouse.shipment.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.vo.SignatureSnapshot;

public class Signature {

    private SignatureId signatureId;
    private String signerName;
    private Instant signedAt;
    private SignatureMethod signatureMethod;
    private String documentReference;
    private ShipmentId shipmentId;
    private byte[] signature;

    public Signature() {
    }

    public Signature(final SignatureId signatureId,
                     final String signerName,
                     final Instant signedAt,
                     final SignatureMethod signatureMethod,
                     final String documentReference,
                     final ShipmentId shipmentId,
                     final byte[] signature) {
        this.signatureId = signatureId;
        this.signerName = signerName;
        this.signedAt = signedAt;
        this.signatureMethod = signatureMethod;
        this.documentReference = documentReference;
        this.shipmentId = shipmentId;
        this.signature = signature;
    }

    public Signature(final String signerName,
                     final Instant signedAt,
                     final SignatureMethod signatureMethod,
                     final String documentReference,
                     final ShipmentId shipmentId,
                     final byte[] signature) {
        this(SignatureId.nextId(), signerName, signedAt, signatureMethod, documentReference, shipmentId, signature);
    }

    public Signature(final String signerName,
                     final SignatureMethod signatureMethod,
                     final String documentReference,
                     final ShipmentId shipmentId,
                     final byte[] signature) {
        this(SignatureId.nextId(), signerName, Instant.now(), signatureMethod,
                documentReference, shipmentId, signature);
    }

    public static Signature from(final SignatureSnapshot snapshot) {
        return new Signature(snapshot.signatureId(), snapshot.signerName(), snapshot.signedAt(), snapshot.signatureMethod(),
                snapshot.documentReference(), snapshot.shipmentId(), snapshot.signature());
    }

    public SignatureSnapshot snapshot() {
        return new SignatureSnapshot(signatureId, shipmentId, signerName, documentReference, signatureMethod, signedAt, signature);
    }

    public SignatureId getSignatureId() {
        return signatureId;
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
