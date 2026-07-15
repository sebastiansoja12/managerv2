package com.warehouse.pickuppoint.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.exception.PickupPointCodeExistsException;
import com.warehouse.pickuppoint.application.exception.PickupPointDepartmentNotFoundException;
import com.warehouse.pickuppoint.application.exception.PickupPointNotFoundException;
import com.warehouse.pickuppoint.application.port.primary.command.ChangePickupPointStatusCommand;
import com.warehouse.pickuppoint.application.port.primary.command.CreatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.command.PickupPointConfigurationCommand;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;
import com.warehouse.pickuppoint.application.port.primary.command.UpdatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointListItemResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointPageResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointResult;
import com.warehouse.pickuppoint.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointCoordinatesServicePort;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchItem;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchPage;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchRepository;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.event.PickupPointClosed;
import com.warehouse.pickuppoint.domain.event.PickupPointCreated;
import com.warehouse.pickuppoint.domain.event.PickupPointResumed;
import com.warehouse.pickuppoint.domain.event.PickupPointSuspended;
import com.warehouse.pickuppoint.domain.event.PickupPointUpdated;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;
import com.warehouse.pickuppoint.domain.vo.PickupPointSelectionCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PickupPointPortImpl implements PickupPointPort {

    private final PickupPointRepository pickupPointRepository;
    private final PickupPointSearchRepository pickupPointSearchRepository;
    private final DepartmentDirectoryServicePort departmentDirectoryServicePort;
    private final PickupPointCoordinatesServicePort pickupPointCoordinatesServicePort;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public PickupPointPortImpl(
            final PickupPointRepository pickupPointRepository,
            final PickupPointSearchRepository pickupPointSearchRepository,
            final DepartmentDirectoryServicePort departmentDirectoryServicePort,
            final PickupPointCoordinatesServicePort pickupPointCoordinatesServicePort,
            final DomainEventPublisher domainEventPublisher,
            final Clock clock) {
        this.pickupPointRepository = pickupPointRepository;
        this.pickupPointSearchRepository = pickupPointSearchRepository;
        this.departmentDirectoryServicePort = departmentDirectoryServicePort;
        this.pickupPointCoordinatesServicePort = pickupPointCoordinatesServicePort;
        this.domainEventPublisher = domainEventPublisher;
        this.clock = clock;
    }

    @Override
    @Transactional
    public PickupPointResult create(final CreatePickupPointCommand command) {
        if (this.pickupPointRepository.existsByCode(command.code())) {
            throw new PickupPointCodeExistsException(command.code());
        }
        final Instant timestamp = this.clock.instant();
        final PickupPointConfigurationCommand configuration = command.configuration();
        final PickupPointDepartment department = departmentRequired(configuration.departmentId());
        if (!department.active()) {
            throw new InvalidPickupPointConfigurationException("Pickup point department must be active");
        }
        final PickupPoint pickupPoint = PickupPoint.create(
                PickupPointId.generate(),
                command.code(),
                configuration.name(),
                configuration.type(),
                configuration.capabilities(),
                configuration.address(),
                this.pickupPointCoordinatesServicePort.getCoordinates(configuration.address()),
                configuration.departmentId(),
                configuration.contact(),
                configuration.accessInstructions(),
                configuration.openingSchedule(),
                configuration.servicePolicy(),
                configuration.externalReference(),
                timestamp);
        final PickupPoint savedPickupPoint = this.pickupPointRepository.save(pickupPoint);
        this.domainEventPublisher.publish(new PickupPointCreated(savedPickupPoint.snapshot(), timestamp));
        return result(savedPickupPoint, timestamp, departmentsById());
    }

    @Override
    @Transactional
    public PickupPointResult update(final UpdatePickupPointCommand command) {
        final PickupPoint pickupPoint = find(command.pickupPointId());
        final boolean requireActiveDepartment = pickupPoint.snapshot().status() == PickupPointStatus.ACTIVE;
        validateDepartment(command.configuration().departmentId(), requireActiveDepartment);
        final Instant timestamp = this.clock.instant();
        if (!configure(pickupPoint, command.configuration(), timestamp)) {
            return result(pickupPoint, timestamp, departmentsById());
        }
        final PickupPoint savedPickupPoint = this.pickupPointRepository.save(pickupPoint);
        this.domainEventPublisher.publish(new PickupPointUpdated(savedPickupPoint.snapshot(), timestamp));
        return result(savedPickupPoint, timestamp, departmentsById());
    }

    @Override
    @Transactional
    public PickupPointResult changeStatus(final ChangePickupPointStatusCommand command) {
        final PickupPoint pickupPoint = find(command.pickupPointId());
        final Instant timestamp = this.clock.instant();
        final boolean changed = changeStatus(pickupPoint, command, timestamp);
        if (!changed) {
            return result(pickupPoint, timestamp, departmentsById());
        }
        final PickupPoint savedPickupPoint = this.pickupPointRepository.save(pickupPoint);
        publishStatusChanged(savedPickupPoint, timestamp);
        return result(savedPickupPoint, timestamp, departmentsById());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PickupPointResult> get(final PickupPointId pickupPointId) {
        final Instant timestamp = this.clock.instant();
        final Map<DepartmentId, PickupPointDepartment> departments = departmentsById();
        return this.pickupPointRepository.findById(pickupPointId)
                .map(pickupPoint -> result(pickupPoint, timestamp, departments));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PickupPointResult> get(final List<PickupPointId> pickupPointIds) {
        final Instant timestamp = this.clock.instant();
        final Map<DepartmentId, PickupPointDepartment> departments = departmentsById();
        return this.pickupPointRepository.findByIds(pickupPointIds).stream()
                .map(pickupPoint -> result(pickupPoint, timestamp, departments))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PickupPointPageResult search(final SearchPickupPointsCommand command) {
        final Instant timestamp = this.clock.instant();
        final Map<DepartmentId, PickupPointDepartment> departments = departmentsById();
        final Set<DepartmentId> eligibleDepartmentIds = command.eligibilitySearch()
                ? departments.values().stream()
                        .filter(PickupPointDepartment::active)
                        .map(PickupPointDepartment::departmentId)
                        .collect(Collectors.toSet())
                : Set.of();
        final PickupPointSearchPage searchPage = this.pickupPointSearchRepository.search(
                command,
                eligibleDepartmentIds);
        final List<PickupPointListItemResult> items = searchPage.items().stream()
                .map(pickupPoint -> listItem(pickupPoint, timestamp, departments))
                .toList();
        return new PickupPointPageResult(
                items,
                command.page(),
                command.size(),
                searchPage.totalElements(),
                searchPage.totalPages(),
                timestamp);
    }

    @Override
    @Transactional
    public PickupPointResult validateSelection(
            final PickupPointId pickupPointId,
            final PickupPointSelectionCriteria criteria) {
        final PickupPoint pickupPoint = this.pickupPointRepository.findByIdForSelection(pickupPointId)
                .orElseThrow(() -> new PickupPointNotFoundException(pickupPointId));
        final PickupPointDepartment department = departmentRequired(pickupPoint.snapshot().departmentId());
        pickupPoint.validateSelection(criteria, department.active());
        return result(pickupPoint, this.clock.instant(), Map.of(department.departmentId(), department));
    }

    private boolean configure(
            final PickupPoint pickupPoint,
            final PickupPointConfigurationCommand configuration,
            final Instant timestamp) {
        boolean changed = pickupPoint.changeType(configuration.type(), timestamp);
        changed = pickupPoint.changeCapabilities(configuration.capabilities(), timestamp) || changed;
        changed = pickupPoint.updateDetails(
                configuration.name(),
                configuration.address(),
                coordinates(pickupPoint, configuration),
                configuration.departmentId(),
                configuration.contact(),
                configuration.accessInstructions(),
                configuration.externalReference(),
                timestamp) || changed;
        changed = pickupPoint.changeOpeningSchedule(configuration.openingSchedule(), timestamp) || changed;
        changed = pickupPoint.changeServicePolicy(configuration.servicePolicy(), timestamp) || changed;
        return changed;
    }

    private GeoCoordinates coordinates(
            final PickupPoint pickupPoint,
            final PickupPointConfigurationCommand configuration) {
        if (Objects.equals(pickupPoint.snapshot().address(), configuration.address())) {
            return pickupPoint.snapshot().coordinates();
        }
        return this.pickupPointCoordinatesServicePort.getCoordinates(configuration.address());
    }

    private boolean changeStatus(
            final PickupPoint pickupPoint,
            final ChangePickupPointStatusCommand command,
            final Instant timestamp) {
        return switch (command.targetStatus()) {
            case ACTIVE -> pickupPoint.resume(
                    departmentRequired(pickupPoint.snapshot().departmentId()).active(),
                    timestamp);
            case SUSPENDED -> pickupPoint.suspend(command.reason(), timestamp);
            case CLOSED -> pickupPoint.close(command.reason(), timestamp);
        };
    }

    private void publishStatusChanged(
            final PickupPoint pickupPoint,
            final Instant timestamp) {
        switch (pickupPoint.snapshot().status()) {
            case ACTIVE -> this.domainEventPublisher.publish(
                    new PickupPointResumed(pickupPoint.snapshot(), timestamp));
            case SUSPENDED -> this.domainEventPublisher.publish(
                    new PickupPointSuspended(pickupPoint.snapshot(), timestamp));
            case CLOSED -> this.domainEventPublisher.publish(new PickupPointClosed(pickupPoint.snapshot(), timestamp));
        }
    }

    private PickupPoint find(final PickupPointId pickupPointId) {
        return this.pickupPointRepository.findById(pickupPointId)
                .orElseThrow(() -> new PickupPointNotFoundException(pickupPointId));
    }

    private void validateDepartment(final DepartmentId departmentId, final boolean requireActive) {
        if (departmentId == null) {
            return;
        }
        final PickupPointDepartment department = departmentRequired(departmentId);
        if (requireActive && !department.active()) {
            throw new InvalidPickupPointConfigurationException("Pickup point department must be active");
        }
    }

    private PickupPointDepartment departmentRequired(final DepartmentId departmentId) {
        if (departmentId == null) {
            throw new InvalidPickupPointConfigurationException("Pickup point department is required");
        }
        return this.departmentDirectoryServicePort.findById(departmentId)
                .orElseThrow(() -> new PickupPointDepartmentNotFoundException(departmentId));
    }

    private PickupPointResult result(
            final PickupPoint pickupPoint,
            final Instant timestamp,
            final Map<DepartmentId, PickupPointDepartment> departments) {
        final DepartmentId departmentId = pickupPoint.snapshot().departmentId();
        final PickupPointDepartment department = departmentId == null ? null : departments.get(departmentId);
        final Boolean openNow = pickupPoint.snapshot().openingSchedule() == null
                ? null
                : pickupPoint.isOpenAt(timestamp);
        return new PickupPointResult(pickupPoint.snapshot(), department, openNow, timestamp);
    }

    private PickupPointListItemResult listItem(
            final PickupPointSearchItem pickupPoint,
            final Instant timestamp,
            final Map<DepartmentId, PickupPointDepartment> departments) {
        return new PickupPointListItemResult(
                pickupPoint,
                departments.get(pickupPoint.departmentId()),
                timestamp);
    }

    private Map<DepartmentId, PickupPointDepartment> departmentsById() {
        return this.departmentDirectoryServicePort.getCurrentOperatorDepartments().stream()
                .collect(Collectors.toMap(PickupPointDepartment::departmentId, Function.identity()));
    }

}
