package com.warehouse.reroute.application.port.primary.result;

import java.time.Instant;

import com.warehouse.commonassets.identificator.TrackingNumber;

public record RerouteRequestAccepted(TrackingNumber trackingNumber, Instant requestedAt) {
}
