package com.warehouse.organisationstructure.deliveryarea.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.CreateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.GenerateDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.ReplaceDepartmentDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.UpdateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.result.DepartmentDeliveryAreas;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaConfigurationServicePort;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaDepartmentServicePort;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaRepository;
import com.warehouse.organisationstructure.deliveryarea.domain.enumeration.DeliveryAreaSource;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DeliveryAreaDepartmentNotFoundException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DeliveryAreaNotFoundException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.NoDepartmentsSelectedException;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.organisationstructure.deliveryarea.domain.service.DeliveryAreaGenerator;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDepartment;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDraft;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class DeliveryAreaPortImpl implements DeliveryAreaPort {

    private final DeliveryAreaRepository deliveryAreaRepository;

    private final DeliveryAreaDepartmentServicePort deliveryAreaDepartmentServicePort;

    private final DeliveryAreaConfigurationServicePort deliveryAreaConfigurationServicePort;

    private final DeliveryAreaGenerator deliveryAreaGenerator;

    private final Clock clock;

    public DeliveryAreaPortImpl(final DeliveryAreaRepository deliveryAreaRepository,
                                final DeliveryAreaDepartmentServicePort deliveryAreaDepartmentServicePort,
                                final DeliveryAreaConfigurationServicePort deliveryAreaConfigurationServicePort,
                                final DeliveryAreaGenerator deliveryAreaGenerator,
                                final Clock clock) {
        this.deliveryAreaRepository = deliveryAreaRepository;
        this.deliveryAreaDepartmentServicePort = deliveryAreaDepartmentServicePort;
        this.deliveryAreaConfigurationServicePort = deliveryAreaConfigurationServicePort;
        this.deliveryAreaGenerator = deliveryAreaGenerator;
        this.clock = clock;
    }

    @Override
    public List<DeliveryArea> findAll() {
        return deliveryAreaRepository.findAll();
    }

    @Override
    public List<DeliveryArea> findByDepartment(final DepartmentId departmentId) {
        loadDepartment(departmentId);
        return deliveryAreaRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional
    public DeliveryArea create(final CreateDeliveryAreaCommand command) {
        loadDepartment(command.departmentId());
        return deliveryAreaRepository.create(new DeliveryArea(command.departmentId(), command.name(),
                command.boundary(), DeliveryAreaSource.MANUAL, clock.instant()));
    }

    @Override
    @Transactional
    public DeliveryArea update(final UpdateDeliveryAreaCommand command) {
        final DeliveryArea deliveryArea = loadArea(command.deliveryAreaId());
        deliveryArea.update(command.name(), command.boundary(), clock.instant());
        return deliveryAreaRepository.update(deliveryArea);
    }

    @Override
    @Transactional
    public void delete(final DeliveryAreaId deliveryAreaId) {
        loadArea(deliveryAreaId);
        deliveryAreaRepository.delete(deliveryAreaId);
    }

    @Override
    @Transactional
    public List<DeliveryArea> replaceDepartmentAreas(final ReplaceDepartmentDeliveryAreasCommand command) {
        loadDepartment(command.departmentId());
        final Instant now = clock.instant();
        final Map<DeliveryAreaId, DeliveryArea> existing = new LinkedHashMap<>();
        deliveryAreaRepository.findByDepartmentId(command.departmentId())
                .forEach(area -> existing.put(area.getDeliveryAreaId(), area));

        final List<DeliveryArea> result = new ArrayList<>();
        for (final DeliveryAreaDraft draft : command.areas()) {
            final DeliveryArea current = draft.deliveryAreaId() != null ? existing.remove(draft.deliveryAreaId()) : null;
            if (current == null) {
                result.add(deliveryAreaRepository.create(new DeliveryArea(command.departmentId(), draft.name(),
                        draft.boundary(), DeliveryAreaSource.MANUAL, now)));
            } else {
                current.update(draft.name(), draft.boundary(), now);
                result.add(deliveryAreaRepository.update(current));
            }
        }
        existing.keySet().forEach(deliveryAreaRepository::delete);
        log.info("Delivery areas of department {} replaced: {} kept or saved, {} removed",
                command.departmentId().getValue(), result.size(), existing.size());
        return result;
    }

    @Override
    public List<DepartmentDeliveryAreas> previewAutomatic(final GenerateDeliveryAreasCommand command) {
        return generate(command).entrySet().stream()
                .map(entry -> new DepartmentDeliveryAreas(entry.getKey().departmentId(),
                        entry.getKey().departmentCode(), entry.getValue()))
                .toList();
    }

    @Override
    @Transactional
    public List<DepartmentDeliveryAreas> applyAutomatic(final GenerateDeliveryAreasCommand command) {
        final List<DepartmentDeliveryAreas> result = replaceWithGenerated(generate(command));
        log.info("Automatic delivery areas applied for {} departments", result.size());
        return result;
    }

    @Override
    @Transactional
    public void assignAutomaticallyAfterDepartmentCreated(final DepartmentId departmentId) {
        if (!deliveryAreaConfigurationServicePort.isAutomaticAssignmentEnabled()) {
            return;
        }
        final Map<DepartmentId, DeliveryAreaDepartment> departments = new LinkedHashMap<>();
        deliveryAreaDepartmentServicePort.findDepartments()
                .forEach(department -> departments.put(department.departmentId(), department));
        deliveryAreaDepartmentServicePort.findDepartment(departmentId)
                .ifPresent(department -> departments.put(department.departmentId(), department));
        final List<DeliveryAreaDepartment> eligible = departments.values().stream()
                .filter(deliveryAreaGenerator::isEligible)
                .toList();
        if (eligible.isEmpty()) {
            return;
        }
        final List<DepartmentDeliveryAreas> result = replaceWithGenerated(
                deliveryAreaGenerator.generate(eligible, clock.instant()));
        log.info("Delivery areas reassigned for {} departments after department {} was created",
                result.size(), departmentId.getValue());
    }

    @Override
    @Transactional
    public void reassignAfterDepartmentDeleted(final DepartmentId departmentId) {
        deliveryAreaRepository.findByDepartmentId(departmentId)
                .forEach(area -> deliveryAreaRepository.delete(area.getDeliveryAreaId()));
        if (!deliveryAreaConfigurationServicePort.isAutomaticAssignmentEnabled()) {
            return;
        }
        final List<DeliveryAreaDepartment> eligible = deliveryAreaDepartmentServicePort.findDepartments().stream()
                .filter(department -> !department.departmentId().equals(departmentId))
                .filter(deliveryAreaGenerator::isEligible)
                .toList();
        if (eligible.isEmpty()) {
            return;
        }
        final List<DepartmentDeliveryAreas> result = replaceWithGenerated(
                deliveryAreaGenerator.generate(eligible, clock.instant()));
        log.info("Delivery areas reassigned for {} departments after department {} was deleted",
                result.size(), departmentId.getValue());
    }

    @Override
    public Optional<DepartmentId> findServingDepartment(final GeoPoint location) {
        return deliveryAreaRepository.findAll().stream()
                .filter(area -> area.covers(location))
                .sorted(Comparator.comparingDouble(area -> area.getBoundary().area()))
                .map(DeliveryArea::getDepartmentId)
                .filter(departmentId -> deliveryAreaDepartmentServicePort.findDepartment(departmentId)
                        .map(DeliveryAreaDepartment::active)
                        .orElse(false))
                .findFirst();
    }

    private List<DepartmentDeliveryAreas> replaceWithGenerated(
            final Map<DeliveryAreaDepartment, List<DeliveryArea>> generated) {
        return generated.entrySet().stream()
                .map(entry -> {
                    final DepartmentId departmentId = entry.getKey().departmentId();
                    deliveryAreaRepository.findByDepartmentId(departmentId)
                            .forEach(area -> deliveryAreaRepository.delete(area.getDeliveryAreaId()));
                    final List<DeliveryArea> saved = entry.getValue().stream()
                            .map(deliveryAreaRepository::create)
                            .toList();
                    return new DepartmentDeliveryAreas(departmentId, entry.getKey().departmentCode(), saved);
                })
                .toList();
    }

    private Map<DeliveryAreaDepartment, List<DeliveryArea>> generate(final GenerateDeliveryAreasCommand command) {
        if (command.departmentIds() == null || command.departmentIds().isEmpty()) {
            throw new NoDepartmentsSelectedException();
        }
        final List<DeliveryAreaDepartment> departments = new LinkedHashSet<>(command.departmentIds()).stream()
                .filter(Objects::nonNull)
                .map(this::loadDepartment)
                .toList();
        return deliveryAreaGenerator.generate(departments, clock.instant());
    }

    private DeliveryAreaDepartment loadDepartment(final DepartmentId departmentId) {
        return deliveryAreaDepartmentServicePort.findDepartment(departmentId)
                .orElseThrow(() -> new DeliveryAreaDepartmentNotFoundException(departmentId));
    }

    private DeliveryArea loadArea(final DeliveryAreaId deliveryAreaId) {
        return deliveryAreaRepository.findById(deliveryAreaId)
                .orElseThrow(() -> new DeliveryAreaNotFoundException(deliveryAreaId));
    }
}
