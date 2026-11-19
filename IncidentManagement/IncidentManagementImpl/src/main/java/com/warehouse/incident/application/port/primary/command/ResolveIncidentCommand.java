package com.warehouse.incident.application.port.primary.command;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.incident.domain.enumeration.IncidentResolution;
import com.warehouse.incident.domain.vo.IncidentRepackaging;

public record ResolveIncidentCommand(IncidentId incidentId, IncidentResolution resolution, String note,
                                     IncidentRepackaging repackaging) {
}
