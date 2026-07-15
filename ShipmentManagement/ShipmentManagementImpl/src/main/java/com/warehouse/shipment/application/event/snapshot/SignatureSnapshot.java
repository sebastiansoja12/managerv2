package com.warehouse.shipment.application.event.snapshot;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.model.Signature;

public record SignatureSnapshot(
        ShipmentId shipmentId,
        String signerName,
        String documentReference,
        SignatureMethod signatureMethod,
        Instant signedAt,
        byte[] signature
) {

    public static SignatureSnapshot from(final Signature signature) {
        if (signature == null) {
            return null;
        }
        return new SignatureSnapshot(
                signature.getShipmentId(),
                signature.getSignerName(),
                signature.getDocumentReference(),
                signature.getSignatureMethod(),
                signature.getSignedAt(),
                signature.getSignature()
        );
    }
}
