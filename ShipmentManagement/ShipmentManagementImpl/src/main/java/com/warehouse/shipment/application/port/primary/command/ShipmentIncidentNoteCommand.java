package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.vo.ShipmentNoteIncidentSource;

import java.time.Instant;

public record ShipmentIncidentNoteCommand(ShipmentId shipmentId,
                                          ShipmentNoteIncidentSource incidentSource,
                                          String text,
                                          UserId authorUserId,
                                          Instant occurredAt) {
}
