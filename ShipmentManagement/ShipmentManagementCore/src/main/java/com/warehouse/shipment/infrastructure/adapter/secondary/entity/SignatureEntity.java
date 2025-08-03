package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.model.Signature;

import jakarta.persistence.*;

@Entity
@Table(name = "signature")
public class SignatureEntity {

    @Column(name = "signer_name", nullable = false)
    private String signerName;
    
    @Column(name = "signed_at", nullable = false)
    private Instant signedAt;
    
    @Column(name = "signature_method", nullable = false)
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private SignatureMethod signatureMethod;
    
    @Column(name = "document_reference", nullable = false)
    private String documentReference;

    @Column(name = "shipment_id", nullable = false)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Column(name = "signature", nullable = false)
    private byte[] signature;

    public SignatureEntity() {
    }

    public SignatureEntity(final String signerName,
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
    
    public static SignatureEntity from(final Signature signature) {
		return new SignatureEntity(signature.getSignerName(), signature.getSignedAt(), signature.getSignatureMethod(),
				signature.getDocumentReference(), signature.getShipmentId(), signature.getSignature());
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public SignatureMethod getSignatureMethod() {
        return signatureMethod;
    }

    public Instant getSignedAt() {
        return signedAt;
    }

    public String getSignerName() {
        return signerName;
    }

    public byte[] getSignature() {
        return signature;
    }
}

