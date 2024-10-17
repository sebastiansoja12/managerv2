package com.warehouse.shipment.domain.model;

import java.time.LocalDateTime;

public class Signature {

    private String signerName;
    private LocalDateTime signedAt;
    private String signatureMethod;
    private String documentReference;

    public Signature(final String signerName, final LocalDateTime signedAt,
                     final String signatureMethod, final String documentReference) {
        this.signerName = signerName;
        this.signedAt = signedAt;
        this.signatureMethod = signatureMethod;
        this.documentReference = documentReference;
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

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public void setSignatureMethod(final String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(final String documentReference) {
        this.documentReference = documentReference;
    }
}

