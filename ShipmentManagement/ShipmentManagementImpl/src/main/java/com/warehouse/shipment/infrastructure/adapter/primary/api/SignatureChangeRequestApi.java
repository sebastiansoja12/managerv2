package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record SignatureChangeRequestApi(ShipmentIdDto shipmentId, String signerName, String documentReference,
                                        String signature) {
}
