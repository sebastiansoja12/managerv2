package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

import java.time.Instant;

public record SignatureResponseDto(String signatureId, String signerName, Instant signedAt, String signatureMethod,
                                   String documentReference, String signature) {
}
