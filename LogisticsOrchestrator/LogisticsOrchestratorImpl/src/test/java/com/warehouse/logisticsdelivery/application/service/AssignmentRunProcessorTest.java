package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunMetrics;
import com.warehouse.logisticsdelivery.application.port.secondary.AwaitingCourierDelivery;
import com.warehouse.logisticsdelivery.application.port.secondary.AwaitingCourierQuery;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentRunRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.model.CourierRoster;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.support.TransactionOperations;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.ARRIVED_AT;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.KATOWICE_COURIER;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.RUN_ID;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.WARSAW;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.awaitingCourierAtDestination;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.processingRun;
import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AssignmentRunProcessorTest {

    private static final String WORKER = "worker-1";

    private DepartmentAssignmentRunRepository runRepository;
    private DeliveryRepository deliveryRepository;
    private ShipmentServicePort shipmentServicePort;
    private CourierRosterLoader courierRosterLoader;
    private AssignmentRunMetrics metrics;
    private AssignmentRunProcessor processor;
    private DepartmentAssignmentRun run;

    @BeforeEach
    void setUp() {
        runRepository = mock(DepartmentAssignmentRunRepository.class);
        deliveryRepository = mock(DeliveryRepository.class);
        shipmentServicePort = mock(ShipmentServicePort.class);
        courierRosterLoader = mock(CourierRosterLoader.class);
        metrics = mock(AssignmentRunMetrics.class);
        final Clock clock = Clock.fixed(Instant.parse("2026-11-24T13:00:00Z"), WARSAW);
        processor = new AssignmentRunProcessor(runRepository, deliveryRepository, courierRosterLoader,
                new AssignmentBatchAssigner(deliveryRepository, shipmentServicePort, mock(DomainEventPublisher.class),
                        clock), metrics, TransactionOperations.withoutTransaction(), clock, WORKER,
                Duration.ofMinutes(5), 3, 10);
        run = processingRun();
        when(runRepository.claim(eq(RUN_ID), eq(WORKER), any(), any())).thenReturn(true);
        when(runRepository.findById(RUN_ID)).thenReturn(Optional.of(run));
        when(courierRosterLoader.load(DESTINATION_DEPARTMENT_ID))
                .thenReturn(new CourierRoster(List.of(KATOWICE_COURIER), Map.of()));
        when(deliveryRepository.assignCourier(any())).thenReturn(true);
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of(
                new ShipmentId(1L), "40-001", new ShipmentId(2L), "40-001", new ShipmentId(3L), "40-001"));
    }

    @Test
    void shouldNotProcessRunLeasedByAnotherWorker() {
        when(runRepository.claim(eq(RUN_ID), eq(WORKER), any(), any())).thenReturn(false);

        processor.process(RUN_ID);

        verify(runRepository, never()).findById(any());
    }

    @Test
    void shouldProcessBatchesWithKeysetCursorUntilWindowIsExhausted() {
        final Delivery first = awaitingCourierAtDestination("d-1", new ShipmentId(1L));
        final Delivery second = awaitingCourierAtDestination("d-2", new ShipmentId(2L));
        final Delivery third = awaitingCourierAtDestination("d-3", new ShipmentId(3L));
        when(deliveryRepository.findAwaitingCourier(any()))
                .thenReturn(List.of(candidate(first), candidate(second)))
                .thenReturn(List.of(candidate(third)));
        final ArgumentCaptor<AwaitingCourierQuery> queries = ArgumentCaptor.forClass(AwaitingCourierQuery.class);

        processor.process(RUN_ID);

        verify(deliveryRepository, org.mockito.Mockito.times(2)).findAwaitingCourier(queries.capture());
        assertThat(queries.getAllValues().get(0).after(), nullValue());
        assertThat(queries.getAllValues().get(1).after(), equalTo(position(second)));
        assertThat(run.getStatus(), is(DepartmentAssignmentRunStatus.COMPLETED));
        assertThat(run.getSummary().assigned(), is(3L));
        assertThat(run.getSummary().batches(), is(2));
        verify(metrics).recordRun(any(), any());
    }

    @Test
    void shouldRegisterRetryAndKeepCommittedCursorWhenBatchFails() {
        final DepartmentAssignmentRun committed = processingRun();
        when(runRepository.findById(RUN_ID)).thenReturn(Optional.of(run)).thenReturn(Optional.of(committed));
        when(deliveryRepository.findAwaitingCourier(any())).thenThrow(new IllegalStateException("deadlock"));

        processor.process(RUN_ID);

        assertThat(committed.getStatus(), is(DepartmentAssignmentRunStatus.PENDING));
        assertThat(committed.getRetries(), is(1));
        assertThat(committed.getCursor(), nullValue());
        verify(runRepository).save(committed);
    }

    @Test
    void shouldStopRunWhenDepartmentHasNoCouriers() {
        when(courierRosterLoader.load(DESTINATION_DEPARTMENT_ID)).thenReturn(new CourierRoster(List.of(), Map.of()));

        processor.process(RUN_ID);

        assertThat(run.getStatus(), is(DepartmentAssignmentRunStatus.FAILED));
        verify(deliveryRepository, never()).findAwaitingCourier(any());
    }

    private static AwaitingCourierDelivery candidate(final Delivery delivery) {
        return new AwaitingCourierDelivery(delivery, position(delivery));
    }

    private static DeliveryAssignmentCursor position(final Delivery delivery) {
        return new DeliveryAssignmentCursor(ARRIVED_AT, delivery.getDeliveryId());
    }
}
