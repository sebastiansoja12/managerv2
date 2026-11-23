package com.warehouse.pickuppoint.application.port.primary;

import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.exception.PickupPointCodeExistsException;
import com.warehouse.pickuppoint.application.exception.PickupPointNotFoundException;
import com.warehouse.pickuppoint.application.port.primary.command.ChangePickupPointStatusCommand;
import com.warehouse.pickuppoint.application.port.primary.command.CreatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;
import com.warehouse.pickuppoint.application.port.primary.command.UpdatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointPageResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointResult;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointRepository;
import com.warehouse.pickuppoint.application.service.PickupPointConfigurationService;
import com.warehouse.pickuppoint.application.service.PickupPointDepartmentService;
import com.warehouse.pickuppoint.application.service.PickupPointResultFactory;
import com.warehouse.pickuppoint.application.service.PickupPointSearchService;
import com.warehouse.pickuppoint.application.service.PickupPointStatusService;
import com.warehouse.pickuppoint.domain.event.PickupPointChanged;
import com.warehouse.pickuppoint.domain.event.PickupPointCreated;
import com.warehouse.pickuppoint.domain.event.PickupPointDeleted;
import com.warehouse.pickuppoint.domain.event.PickupPointUpdated;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;
import com.warehouse.pickuppoint.domain.vo.PickupPointSelectionCriteria;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PickupPointPortImpl implements PickupPointPort {

    private final PickupPointRepository pickupPointRepository;
    private final PickupPointDepartmentService pickupPointDepartmentService;
    private final PickupPointConfigurationService pickupPointConfigurationService;
    private final PickupPointStatusService pickupPointStatusService;
    private final PickupPointSearchService pickupPointSearchService;
    private final PickupPointResultFactory pickupPointResultFactory;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public PickupPointPortImpl(
            final PickupPointRepository pickupPointRepository,
            final PickupPointDepartmentService pickupPointDepartmentService,
            final PickupPointConfigurationService pickupPointConfigurationService,
            final PickupPointStatusService pickupPointStatusService,
            final PickupPointSearchService pickupPointSearchService,
            final PickupPointResultFactory pickupPointResultFactory,
            final DomainEventPublisher domainEventPublisher,
            final Clock clock) {
        this.pickupPointRepository = pickupPointRepository;
        this.pickupPointDepartmentService = pickupPointDepartmentService;
        this.pickupPointConfigurationService = pickupPointConfigurationService;
        this.pickupPointStatusService = pickupPointStatusService;
        this.pickupPointSearchService = pickupPointSearchService;
        this.pickupPointResultFactory = pickupPointResultFactory;
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
        this.pickupPointDepartmentService.ensureActive(command.configuration().departmentId());
        final PickupPoint pickupPoint = this.pickupPointConfigurationService.create(
                command.code(),
                command.configuration(),
                timestamp);
        final PickupPoint savedPickupPoint = this.pickupPointRepository.save(pickupPoint);
        this.domainEventPublisher.publish(new PickupPointCreated(savedPickupPoint.snapshot(), timestamp));
        return this.pickupPointResultFactory.create(savedPickupPoint, timestamp);
    }

    @Override
    @Transactional
    public PickupPointResult update(final UpdatePickupPointCommand command) {
        final PickupPoint pickupPoint = find(command.pickupPointId());
        final PickupPointSnapshot previousSnapshot = pickupPoint.snapshot();
        this.pickupPointDepartmentService.ensureAssignable(
                command.configuration().departmentId(),
                previousSnapshot.status());
        final Instant timestamp = this.clock.instant();
        this.pickupPointConfigurationService.configure(pickupPoint, command.configuration(), timestamp);
        final PickupPoint currentPickupPoint = saveWhenChanged(pickupPoint, previousSnapshot,
                snapshot -> new PickupPointUpdated(previousSnapshot, snapshot, timestamp));
        return this.pickupPointResultFactory.create(currentPickupPoint, timestamp);
    }

    @Override
    @Transactional
    public PickupPointResult changeStatus(final ChangePickupPointStatusCommand command) {
        final PickupPoint pickupPoint = find(command.pickupPointId());
        final Instant timestamp = this.clock.instant();
        final PickupPointSnapshot previousSnapshot = pickupPoint.snapshot();
        this.pickupPointStatusService.changeStatus(pickupPoint, command, timestamp);
        final PickupPoint currentPickupPoint = saveWhenChanged(pickupPoint, previousSnapshot,
                snapshot -> this.pickupPointStatusService.statusChangedEvent(previousSnapshot, snapshot, timestamp));
        return this.pickupPointResultFactory.create(currentPickupPoint, timestamp);
    }

    @Override
    @Transactional
    public void delete(final PickupPointId pickupPointId) {
        final PickupPoint pickupPoint = find(pickupPointId);
        final PickupPointSnapshot previousSnapshot = pickupPoint.snapshot();
        final Instant timestamp = this.clock.instant();
        pickupPoint.delete(timestamp);
        saveWhenChanged(pickupPoint, previousSnapshot,
                snapshot -> new PickupPointDeleted(previousSnapshot, snapshot, timestamp));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PickupPointResult> get(final PickupPointId pickupPointId) {
        final Instant timestamp = this.clock.instant();
        final Map<DepartmentId, PickupPointDepartment> departments =
                this.pickupPointDepartmentService.getCurrentOperatorDepartments();
        return this.pickupPointRepository.findById(pickupPointId)
                .map(pickupPoint -> this.pickupPointResultFactory.create(pickupPoint, departments, timestamp));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PickupPointResult> get(final List<PickupPointId> pickupPointIds) {
        final Instant timestamp = this.clock.instant();
        final Map<DepartmentId, PickupPointDepartment> departments =
                this.pickupPointDepartmentService.getCurrentOperatorDepartments();
        return this.pickupPointRepository.findByIds(pickupPointIds).stream()
                .map(pickupPoint -> this.pickupPointResultFactory.create(pickupPoint, departments, timestamp))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PickupPointPageResult search(final SearchPickupPointsCommand command) {
        return this.pickupPointSearchService.search(command, this.clock.instant());
    }

    @Override
    @Transactional
    public PickupPointResult validateSelection(
            final PickupPointId pickupPointId,
            final PickupPointSelectionCriteria criteria) {
        final PickupPoint pickupPoint = this.pickupPointRepository.findByIdForSelection(pickupPointId)
                .orElseThrow(() -> new PickupPointNotFoundException(pickupPointId));
        final PickupPointDepartment department =
                this.pickupPointDepartmentService.getRequired(pickupPoint.snapshot().departmentId());
        pickupPoint.validateSelection(criteria, department.active());
        return this.pickupPointResultFactory.create(pickupPoint, department, this.clock.instant());
    }

    private PickupPoint saveWhenChanged(
            final PickupPoint pickupPoint,
            final PickupPointSnapshot previousSnapshot,
            final Function<PickupPointSnapshot, PickupPointChanged> changedEvent) {
        if (pickupPoint.snapshot().equals(previousSnapshot)) {
            return pickupPoint;
        }
        final PickupPoint savedPickupPoint = this.pickupPointRepository.save(pickupPoint);
        this.domainEventPublisher.publish(changedEvent.apply(savedPickupPoint.snapshot()));
        return savedPickupPoint;
    }

    private PickupPoint find(final PickupPointId pickupPointId) {
        return this.pickupPointRepository.findById(pickupPointId)
                .orElseThrow(() -> new PickupPointNotFoundException(pickupPointId));
    }
}
