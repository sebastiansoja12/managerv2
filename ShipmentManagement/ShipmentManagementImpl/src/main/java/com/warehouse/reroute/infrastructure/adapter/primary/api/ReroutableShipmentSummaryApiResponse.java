package com.warehouse.reroute.infrastructure.adapter.primary.api;

public record ReroutableShipmentSummaryApiResponse(String trackingNumber,
                                                   String status,
                                                   boolean reroutable,
                                                   String maskedEmail) {
}
