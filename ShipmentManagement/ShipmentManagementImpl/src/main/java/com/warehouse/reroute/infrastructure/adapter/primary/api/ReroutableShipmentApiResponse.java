package com.warehouse.reroute.infrastructure.adapter.primary.api;

import java.time.Instant;

public record ReroutableShipmentApiResponse(String trackingNumber,
                                            String status,
                                            boolean reroutable,
                                            ReroutePartyApi sender,
                                            ReroutePartyApi recipient,
                                            Instant tokenExpiresAt) {
}
