package com.warehouse.incident.application.port.primary;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.incident.application.port.primary.command.CommentIncidentCommand;
import com.warehouse.incident.application.port.primary.command.FindIncidentsCommand;
import com.warehouse.incident.application.port.primary.command.ReopenIncidentCommand;
import com.warehouse.incident.application.port.primary.command.ReportIncidentCommand;
import com.warehouse.incident.application.port.primary.command.ResolveIncidentCommand;
import com.warehouse.incident.application.port.primary.result.IncidentPage;
import com.warehouse.incident.application.port.primary.result.IncidentStatistics;
import com.warehouse.incident.domain.model.Incident;

public interface IncidentPort {

    IncidentId report(final ReportIncidentCommand command);

    void assignToCurrentUser(final IncidentId incidentId);

    void comment(final CommentIncidentCommand command);

    void resolve(final ResolveIncidentCommand command);

    void reopen(final ReopenIncidentCommand command);

    Incident get(final IncidentId incidentId);

    IncidentPage find(final FindIncidentsCommand command);

    IncidentStatistics statistics();
}
