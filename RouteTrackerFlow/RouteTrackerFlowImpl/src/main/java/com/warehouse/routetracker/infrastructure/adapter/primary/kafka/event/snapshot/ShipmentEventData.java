package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentEventData(
        ShipmentId shipmentId,
        PartySnapshot sender,
        PartySnapshot recipient,
        ShipmentStatus shipmentStatus,
        ShipmentType shipmentType,
        ShipmentId shipmentRelatedId,
        MoneySnapshot price,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime updatedAt,
        Boolean locked,
        Boolean signatureRequired,
        ShipmentPriority shipmentPriority,
        ShipmentId signatureId,
        TrackingNumber trackingNumber,
        ExternalId<UUID> externalShipmentId,
        String serviceLevel,
        String packagingType
) {
}
