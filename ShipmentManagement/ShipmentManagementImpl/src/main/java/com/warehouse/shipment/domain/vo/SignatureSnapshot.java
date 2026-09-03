package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;

import java.time.Instant;

public record SignatureSnapshot(SignatureId signatureId, ShipmentId shipmentId, String signerName, String documentReference, SignatureMethod signatureMethod,
                                Instant signedAt, byte[] signature) {
}
