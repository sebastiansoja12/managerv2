package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.logisticsdelivery.domain.enumeration.SignatureMethod;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "delivery.SignatureEntity")
@Table(name = "signature")
public class SignatureEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "signature_id", nullable = false))
    private SignatureId signatureId;

    @Column(name = "signer_name", nullable = false)
    private String signerName;

    @Column(name = "signed_at", nullable = false)
    private Instant signedAt;

    @Column(name = "signature_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private SignatureMethod signatureMethod;

    @Column(name = "document_reference")
    private String documentReference;

    @Column(name = "signature", nullable = false)
    private byte[] signature;

    protected SignatureEntity() {
    }

    public SignatureEntity(final SignatureId signatureId,
                           final String signerName,
                           final Instant signedAt,
                           final SignatureMethod signatureMethod,
                           final String documentReference,
                           final byte[] signature) {
        this.signatureId = signatureId;
        this.signerName = signerName;
        this.signedAt = signedAt;
        this.signatureMethod = signatureMethod;
        this.documentReference = documentReference;
        this.signature = signature;
    }

    public SignatureId getSignatureId() {
        return signatureId;
    }

    public String getSignerName() {
        return signerName;
    }

    public Instant getSignedAt() {
        return signedAt;
    }

    public SignatureMethod getSignatureMethod() {
        return signatureMethod;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public byte[] getSignature() {
        return signature;
    }
}
