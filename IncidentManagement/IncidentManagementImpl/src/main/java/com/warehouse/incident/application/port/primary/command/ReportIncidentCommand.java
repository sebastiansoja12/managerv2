package com.warehouse.incident.application.port.primary.command;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;

public record ReportIncidentCommand(ShipmentId shipmentId,
                                    IncidentCategory category,
                                    IncidentSeverity severity,
                                    String description) {
}
