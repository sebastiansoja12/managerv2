package com.warehouse.incident.application.port.secondary;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.incident.application.port.primary.command.FindIncidentsCommand;
import com.warehouse.incident.application.port.primary.result.IncidentPage;
import com.warehouse.incident.application.port.primary.result.IncidentStatistics;
import com.warehouse.incident.domain.model.Incident;

import java.time.Instant;
import java.util.Optional;

public interface IncidentRepository {

    void create(final Incident incident);

    void update(final Incident incident);

    Optional<Incident> findById(final IncidentId incidentId);

    boolean existsUnresolvedForShipment(final ShipmentId shipmentId);

    IncidentPage find(final FindIncidentsCommand command);

    IncidentStatistics statistics(final Instant overdueBefore, final Instant resolvedSince);
}
