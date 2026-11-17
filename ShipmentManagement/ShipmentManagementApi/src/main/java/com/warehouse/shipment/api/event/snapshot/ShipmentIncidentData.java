package com.warehouse.shipment.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentIncidentData(ShipmentId shipmentId,
                                   IncidentId incidentId,
                                   UserId authorUserId,
                                   String reason,
                                   String text,
                                   Instant occurredAt) {
}
