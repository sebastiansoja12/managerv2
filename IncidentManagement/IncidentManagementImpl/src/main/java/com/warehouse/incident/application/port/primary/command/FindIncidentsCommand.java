package com.warehouse.incident.application.port.primary.command;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;

public record FindIncidentsCommand(IncidentStatus status,
                                   IncidentSeverity severity,
                                   IncidentCategory category,
                                   ShipmentId shipmentId,
                                   boolean assignedToMe,
                                   UserId assigneeId,
                                   int page,
                                   int size) {
}
