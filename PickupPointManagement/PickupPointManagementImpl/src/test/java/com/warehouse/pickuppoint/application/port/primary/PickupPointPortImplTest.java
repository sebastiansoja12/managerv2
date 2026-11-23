package com.warehouse.pickuppoint.application.port.primary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.port.primary.command.ChangePickupPointStatusCommand;
import com.warehouse.pickuppoint.application.port.primary.command.CreatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.command.PickupPointConfigurationCommand;
import com.warehouse.pickuppoint.application.port.primary.command.UpdatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointCoordinatesServicePort;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchRepository;
import com.warehouse.pickuppoint.application.service.PickupPointConfigurationService;
import com.warehouse.pickuppoint.application.service.PickupPointDepartmentService;
import com.warehouse.pickuppoint.application.service.PickupPointResultFactory;
import com.warehouse.pickuppoint.application.service.PickupPointSearchService;
import com.warehouse.pickuppoint.application.service.PickupPointStatusService;
import com.warehouse.pickuppoint.domain.enumeration.OpeningScheduleMode;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.event.PickupPointClosed;
import com.warehouse.pickuppoint.domain.event.PickupPointDeleted;
import com.warehouse.pickuppoint.domain.event.PickupPointUpdated;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointStatusTransitionException;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.OpeningSchedule;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;
import com.warehouse.pickuppoint.domain.vo.PickupPointServicePolicy;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PickupPointPortImplTest {

    private static final Instant CREATED_AT = Instant.parse("2026-09-09T08:00:00Z");
    private static final Instant NOW = Instant.parse("2026-09-09T09:00:00Z");
    private static final String NAME = "Point by the station";
    private static final Set<PickupPointCapability> CAPABILITIES = Set.of(PickupPointCapability.DROP_OFF);
    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);
    private static final PickupPointId PICKUP_POINT_ID =
            new PickupPointId(UUID.fromString("9a7f3b38-73d0-4e9b-91b7-dadfeab4882f"));
    private static final PickupPointAddress ADDRESS =
            new PickupPointAddress(CountryCode.PL, "00-001", "Warsaw", "Example", "10", null);
    private static final GeoCoordinates COORDINATES = new GeoCoordinates(52.2297, 21.0122);
    private static final OpeningSchedule OPENING_SCHEDULE = new OpeningSchedule(
            ZoneId.of("Europe/Warsaw"), OpeningScheduleMode.ALWAYS_OPEN, List.of(), List.of());
    private static final PickupPointServicePolicy SERVICE_POLICY =
            new PickupPointServicePolicy(Set.of(PickupPointShipmentSize.SMALL), false);

    private final PickupPointRepository pickupPointRepository = mock(PickupPointRepository.class);
    private final PickupPointSearchRepository pickupPointSearchRepository = mock(PickupPointSearchRepository.class);
    private final DepartmentDirectoryServicePort departmentDirectoryServicePort =
            mock(DepartmentDirectoryServicePort.class);
    private final PickupPointCoordinatesServicePort pickupPointCoordinatesServicePort =
            mock(PickupPointCoordinatesServicePort.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);
    private final PickupPointDepartmentService pickupPointDepartmentService =
            new PickupPointDepartmentService(departmentDirectoryServicePort);
    private final PickupPointPortImpl pickupPointPort = new PickupPointPortImpl(
            pickupPointRepository,
            pickupPointDepartmentService,
            new PickupPointConfigurationService(pickupPointCoordinatesServicePort),
            new PickupPointStatusService(pickupPointDepartmentService),
            new PickupPointSearchService(pickupPointSearchRepository, pickupPointDepartmentService),
            new PickupPointResultFactory(pickupPointDepartmentService),
            domainEventPublisher,
            Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldRejectCreateWhenDepartmentIsInactive() {
        when(pickupPointRepository.existsByCode(any())).thenReturn(false);
        departmentExists(false);
        final CreatePickupPointCommand command =
                new CreatePickupPointCommand(new PickupPointCode("WAW-PP-002"), configuration(NAME));

        assertThrows(InvalidPickupPointConfigurationException.class, () -> pickupPointPort.create(command));

        verifyNoInteractions(pickupPointCoordinatesServicePort, domainEventPublisher);
        verify(pickupPointRepository, never()).save(any());
    }

    @Test
    void shouldNotSaveNorPublishWhenUpdateChangesNothing() {
        pickupPointExists(pickupPoint());
        departmentExists(true);

        pickupPointPort.update(new UpdatePickupPointCommand(PICKUP_POINT_ID, configuration(NAME)));

        verify(pickupPointRepository, never()).save(any());
        verifyNoInteractions(pickupPointCoordinatesServicePort, domainEventPublisher);
    }

    @Test
    void shouldSaveAndPublishWhenUpdateChangesConfiguration() {
        pickupPointExists(pickupPoint());
        departmentExists(true);
        saveReturnsPickupPoint();

        pickupPointPort.update(new UpdatePickupPointCommand(PICKUP_POINT_ID, configuration("Renamed point")));

        verify(pickupPointRepository).save(any());
        verify(domainEventPublisher).publish(any(PickupPointUpdated.class));
    }

    @Test
    void shouldRejectUpdateOfActivePickupPointWhenDepartmentIsInactive() {
        pickupPointExists(pickupPoint());
        departmentExists(false);
        final UpdatePickupPointCommand command = new UpdatePickupPointCommand(PICKUP_POINT_ID, configuration(NAME));

        assertThrows(InvalidPickupPointConfigurationException.class, () -> pickupPointPort.update(command));

        verify(pickupPointRepository, never()).save(any());
    }

    @Test
    void shouldAllowUpdateOfSuspendedPickupPointWhenDepartmentIsInactive() {
        final PickupPoint pickupPoint = pickupPoint();
        pickupPoint.suspend("Technical maintenance", CREATED_AT.plusSeconds(60));
        pickupPointExists(pickupPoint);
        departmentExists(false);
        saveReturnsPickupPoint();

        pickupPointPort.update(new UpdatePickupPointCommand(PICKUP_POINT_ID, configuration("Renamed point")));

        verify(domainEventPublisher).publish(any(PickupPointUpdated.class));
    }

    @Test
    void shouldNotSaveNorPublishWhenStatusIsAlreadyApplied() {
        final PickupPoint pickupPoint = pickupPoint();
        pickupPoint.suspend("Technical maintenance", CREATED_AT.plusSeconds(60));
        pickupPointExists(pickupPoint);

        pickupPointPort.changeStatus(new ChangePickupPointStatusCommand(
                PICKUP_POINT_ID, PickupPointStatus.SUSPENDED, "Another reason"));

        verify(pickupPointRepository, never()).save(any());
        verifyNoInteractions(domainEventPublisher);
    }

    @Test
    void shouldPublishClosedEventWhenPickupPointIsClosed() {
        final PickupPoint pickupPoint = pickupPoint();
        pickupPointExists(pickupPoint);
        saveReturnsPickupPoint();

        pickupPointPort.changeStatus(new ChangePickupPointStatusCommand(
                PICKUP_POINT_ID, PickupPointStatus.CLOSED, "Location closed"));

        assertEquals(PickupPointStatus.CLOSED, pickupPoint.snapshot().status());
        verify(pickupPointRepository).save(pickupPoint);
        verify(domainEventPublisher).publish(any(PickupPointClosed.class));
    }

    @Test
    void shouldRejectDeletedAsRequestedStatus() {
        pickupPointExists(pickupPoint());
        final ChangePickupPointStatusCommand command =
                new ChangePickupPointStatusCommand(PICKUP_POINT_ID, PickupPointStatus.DELETED, null);

        assertThrows(InvalidPickupPointStatusTransitionException.class, () -> pickupPointPort.changeStatus(command));

        verify(pickupPointRepository, never()).save(any());
        verifyNoInteractions(domainEventPublisher);
    }

    @Test
    void shouldPublishDeletedEventWhenPickupPointIsDeleted() {
        pickupPointExists(pickupPoint());
        saveReturnsPickupPoint();

        pickupPointPort.delete(PICKUP_POINT_ID);

        verify(domainEventPublisher).publish(any(PickupPointDeleted.class));
    }

    @Test
    void shouldNotSaveNorPublishWhenPickupPointIsAlreadyDeleted() {
        final PickupPoint pickupPoint = pickupPoint();
        pickupPoint.delete(CREATED_AT.plusSeconds(60));
        pickupPointExists(pickupPoint);

        pickupPointPort.delete(PICKUP_POINT_ID);

        verify(pickupPointRepository, never()).save(any());
        verifyNoInteractions(domainEventPublisher);
    }

    private void pickupPointExists(final PickupPoint pickupPoint) {
        when(pickupPointRepository.findById(PICKUP_POINT_ID)).thenReturn(Optional.of(pickupPoint));
    }

    private void departmentExists(final boolean active) {
        when(departmentDirectoryServicePort.findById(DEPARTMENT_ID)).thenReturn(Optional.of(
                new PickupPointDepartment(DEPARTMENT_ID, new DepartmentCode("WAW"), active)));
    }

    private void saveReturnsPickupPoint() {
        when(pickupPointRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private static PickupPointConfigurationCommand configuration(final String name) {
        return new PickupPointConfigurationCommand(
                name,
                PickupPointType.SERVICE_POINT,
                CAPABILITIES,
                ADDRESS,
                DEPARTMENT_ID,
                null,
                null,
                OPENING_SCHEDULE,
                SERVICE_POLICY,
                null);
    }

    private static PickupPoint pickupPoint() {
        return PickupPoint.create(
                PICKUP_POINT_ID,
                new PickupPointCode("WAW-PP-001"),
                NAME,
                PickupPointType.SERVICE_POINT,
                CAPABILITIES,
                ADDRESS,
                COORDINATES,
                DEPARTMENT_ID,
                null,
                null,
                OPENING_SCHEDULE,
                SERVICE_POLICY,
                null,
                CREATED_AT);
    }
}
