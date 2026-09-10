package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SignatureSnapshot(
        ShipmentId shipmentId,
        String signerName,
        String documentReference,
        SignatureMethod signatureMethod,
        Instant signedAt,
        byte[] signature
) {
}
