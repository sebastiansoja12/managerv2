package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.time.Instant;

public record SignatureDto(String signerName, Instant signedAt, String signatureMethod, String signature) {
}
