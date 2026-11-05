package com.warehouse.incident.application.port.primary.command;

import com.warehouse.commonassets.identificator.IncidentId;

public record CommentIncidentCommand(IncidentId incidentId, String text) {
}
