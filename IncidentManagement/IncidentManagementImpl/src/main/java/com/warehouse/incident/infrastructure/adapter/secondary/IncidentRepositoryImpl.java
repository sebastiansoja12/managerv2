package com.warehouse.incident.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.incident.application.port.primary.command.FindIncidentsCommand;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.incident.application.port.primary.result.DepartmentIncidentStatistics;
import com.warehouse.incident.application.port.primary.result.IncidentPage;
import com.warehouse.incident.application.port.primary.result.IncidentStatistics;
import com.warehouse.incident.application.port.secondary.IncidentRepository;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentResolution;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;
import com.warehouse.incident.domain.model.Incident;
import com.warehouse.incident.infrastructure.adapter.secondary.entity.IncidentEntity;
import com.warehouse.incident.infrastructure.adapter.secondary.mapper.IncidentPersistenceMapper;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IncidentRepositoryImpl implements IncidentRepository {

    private static final List<IncidentStatus> UNRESOLVED_STATUSES = List.of(IncidentStatus.OPEN,
            IncidentStatus.IN_PROGRESS);

    private final OperatorFilteredRepository<IncidentEntity> repository;

    private final IncidentPersistenceMapper persistenceMapper;

    public IncidentRepositoryImpl(final OperatorFilteredRepository<IncidentEntity> repository,
                                  final IncidentPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void create(final Incident incident) {
        this.repository.create(this.persistenceMapper.toEntity(incident));
    }

    @Override
    public void update(final Incident incident) {
        this.repository.update(this.persistenceMapper.toEntity(incident));
    }

    @Override
    public Optional<Incident> findById(final IncidentId incidentId) {
        return this.repository.createCriteria(IncidentEntity.class)
                .eq("incidentId.value", incidentId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public boolean existsUnresolvedForShipment(final ShipmentId shipmentId) {
        return unresolved()
                .eq("shipmentId.value", shipmentId.getValue())
                .count() > 0;
    }

    @Override
    public IncidentPage find(final FindIncidentsCommand command) {
        final long totalElements = criteria(command).count();
        final List<Incident> incidents = criteria(command)
                .desc("reportedAt")
                .firstResult(command.page() * command.size())
                .maxResults(command.size())
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
        return new IncidentPage(incidents, command.page(), command.size(), totalElements);
    }

    @Override
    public IncidentStatistics statistics(final Instant overdueBefore, final Instant resolvedSince) {
        return new IncidentStatistics(
                unresolved().count(),
                unresolved().eq("severity", IncidentSeverity.CRITICAL).count(),
                unresolved().lt("reportedAt", overdueBefore).count(),
                this.repository.createCriteria(IncidentEntity.class)
                        .eq("status", IncidentStatus.RESOLVED)
                        .ge("resolvedAt", resolvedSince)
                        .count());
    }

    @Override
    public List<DepartmentIncidentStatistics> departmentStatistics(final Instant rangeStart, final Instant rangeEnd) {
        final Map<DepartmentCode, Long> damagedShipments = countByDepartment(
                this.repository.createCriteria(IncidentEntity.class)
                        .eq("category", IncidentCategory.DAMAGED)
                        .ge("reportedAt", rangeStart)
                        .lt("reportedAt", rangeEnd)
                        .list());
        final Map<DepartmentCode, Long> damageClaims = countByDepartment(
                this.repository.createCriteria(IncidentEntity.class)
                        .eq("resolution", IncidentResolution.DAMAGE_CLAIM_FILED)
                        .ge("resolvedAt", rangeStart)
                        .lt("resolvedAt", rangeEnd)
                        .list());
        final Map<DepartmentCode, DepartmentIncidentStatistics> statistics = new LinkedHashMap<>();
        damagedShipments.forEach((departmentCode, count) -> statistics.put(departmentCode,
                new DepartmentIncidentStatistics(departmentCode, count, damageClaims.getOrDefault(departmentCode, 0L))));
        damageClaims.forEach((departmentCode, count) -> statistics.putIfAbsent(departmentCode,
                new DepartmentIncidentStatistics(departmentCode, 0L, count)));
        return List.copyOf(statistics.values());
    }

    private Map<DepartmentCode, Long> countByDepartment(final List<IncidentEntity> incidents) {
        return incidents.stream()
                .filter(incident -> incident.getDepartmentCode() != null)
                .collect(Collectors.groupingBy(IncidentEntity::getDepartmentCode, LinkedHashMap::new,
                        Collectors.counting()));
    }

    private Criteria<IncidentEntity> unresolved() {
        return this.repository.createCriteria(IncidentEntity.class).in("status", UNRESOLVED_STATUSES);
    }

    private Criteria<IncidentEntity> criteria(final FindIncidentsCommand command) {
        final Criteria<IncidentEntity> criteria = this.repository.createCriteria(IncidentEntity.class);
        if (command.status() != null) {
            criteria.eq("status", command.status());
        }
        if (command.severity() != null) {
            criteria.eq("severity", command.severity());
        }
        if (command.category() != null) {
            criteria.eq("category", command.category());
        }
        if (command.shipmentId() != null) {
            criteria.eq("shipmentId.value", command.shipmentId().getValue());
        }
        if (command.assigneeId() != null) {
            criteria.eq("assigneeUserId.value", command.assigneeId().value());
        }
        return criteria;
    }
}
