package com.warehouse.logisticsdelivery.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.logisticsdelivery.domain.enumeration.SignatureMethod;

public class Signature {

    private final SignatureId signatureId;
    private final String signerName;
    private final Instant signedAt;
    private final SignatureMethod signatureMethod;
    private final String documentReference;
    private final byte[] signature;

    public Signature(final SignatureId signatureId,
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

    public Signature(final String signerName,
                     final SignatureMethod signatureMethod,
                     final String documentReference,
                     final byte[] signature) {
        this(SignatureId.nextId(), signerName, Instant.now(), signatureMethod, documentReference, signature);
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
