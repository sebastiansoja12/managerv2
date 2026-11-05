package com.warehouse.incident.application.port.primary;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.incident.application.port.primary.command.CommentIncidentCommand;
import com.warehouse.incident.application.port.primary.command.FindIncidentsCommand;
import com.warehouse.incident.application.port.primary.command.ReopenIncidentCommand;
import com.warehouse.incident.application.port.primary.command.ReportIncidentCommand;
import com.warehouse.incident.application.port.primary.command.ResolveIncidentCommand;
import com.warehouse.incident.application.port.primary.result.IncidentPage;
import com.warehouse.incident.application.port.primary.result.IncidentStatistics;
import com.warehouse.incident.application.port.secondary.IncidentActorServicePort;
import com.warehouse.incident.application.port.secondary.IncidentRepository;
import com.warehouse.incident.application.port.secondary.IncidentShipmentServicePort;
import com.warehouse.incident.domain.exception.IncidentException;
import com.warehouse.incident.domain.model.Incident;
import com.warehouse.incident.domain.vo.IncidentActor;
import com.warehouse.incident.domain.vo.IncidentShipment;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiConsumer;

public class IncidentPortImpl implements IncidentPort {

    private static final int MAX_PAGE_SIZE = 200;

    private static final Duration OVERDUE_AFTER = Duration.ofHours(24);

    private final IncidentRepository incidentRepository;

    private final IncidentActorServicePort incidentActorServicePort;

    private final IncidentShipmentServicePort incidentShipmentServicePort;

    private final Clock clock;

    public IncidentPortImpl(final IncidentRepository incidentRepository,
                            final IncidentActorServicePort incidentActorServicePort,
                            final IncidentShipmentServicePort incidentShipmentServicePort,
                            final Clock clock) {
        this.incidentRepository = incidentRepository;
        this.incidentActorServicePort = incidentActorServicePort;
        this.incidentShipmentServicePort = incidentShipmentServicePort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public IncidentId report(final ReportIncidentCommand command) {
        final IncidentActor actor = currentActor();
        final IncidentShipment shipment = this.incidentShipmentServicePort.getShipment(command.shipmentId());
        if (this.incidentRepository.existsUnresolvedForShipment(shipment.shipmentId())) {
            throw new IncidentException(IncidentException.Reason.CONFLICT,
                    "Shipment already has an unresolved incident");
        }
        final Incident incident = Incident.report(shipment,
                this.incidentActorServicePort.currentDepartmentCode().orElse(null), command.category(),
                command.severity(), command.description(), actor, this.clock.instant());
        this.incidentRepository.create(incident);
        return incident.incidentId();
    }

    @Override
    @Transactional
    public void assignToCurrentUser(final IncidentId incidentId) {
        change(incidentId, (incident, actor) -> incident.assign(actor, this.clock.instant()));
    }

    @Override
    @Transactional
    public void comment(final CommentIncidentCommand command) {
        change(command.incidentId(), (incident, actor) ->
                incident.comment(actor, command.text(), this.clock.instant()));
    }

    @Override
    @Transactional
    public void resolve(final ResolveIncidentCommand command) {
        change(command.incidentId(), (incident, actor) ->
                incident.resolve(actor, command.resolution(), command.note(), this.clock.instant()));
    }

    @Override
    @Transactional
    public void reopen(final ReopenIncidentCommand command) {
        change(command.incidentId(), (incident, actor) ->
                incident.reopen(actor, command.reason(), this.clock.instant()));
    }

    @Override
    public Incident get(final IncidentId incidentId) {
        return this.incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentException(IncidentException.Reason.NOT_FOUND, "Incident not found"));
    }

    @Override
    public IncidentPage find(final FindIncidentsCommand command) {
        return this.incidentRepository.find(new FindIncidentsCommand(command.status(), command.severity(),
                command.category(), command.shipmentId(), command.assignedToMe(),
                command.assignedToMe() ? currentActor().userId() : null,
                Math.max(command.page(), 0), Math.clamp(command.size(), 1, MAX_PAGE_SIZE)));
    }

    @Override
    public IncidentStatistics statistics() {
        final Instant now = this.clock.instant();
        final Instant startOfDay = LocalDate.now(this.clock).atStartOfDay(this.clock.getZone()).toInstant();
        return this.incidentRepository.statistics(now.minus(OVERDUE_AFTER), startOfDay);
    }

    private void change(final IncidentId incidentId, final BiConsumer<Incident, IncidentActor> change) {
        final IncidentActor actor = currentActor();
        final Incident incident = get(incidentId);
        change.accept(incident, actor);
        this.incidentRepository.update(incident);
    }

    private IncidentActor currentActor() {
        return this.incidentActorServicePort.currentActor()
                .orElseThrow(() -> new IncidentException(IncidentException.Reason.ACCESS_DENIED,
                        "Incident operations require a signed-in user"));
    }
}
