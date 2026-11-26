package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentBatch;
import com.warehouse.logisticsdelivery.application.port.secondary.AwaitingCourierDelivery;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.model.CourierRoster;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.ARRIVED_AT;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.KATOWICE_COURIER;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.SECOND_KATOWICE_COURIER;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.WARSAW;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.assignedTo;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.awaitingCourierAtDestination;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.inTransit;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.lastScannedWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AssignmentBatchAssignerTest {

    private static final ShipmentId FIRST = new ShipmentId(1L);
    private static final ShipmentId SECOND = new ShipmentId(2L);

    private DeliveryRepository deliveryRepository;
    private ShipmentServicePort shipmentServicePort;
    private DomainEventPublisher domainEventPublisher;
    private AssignmentBatchAssigner assigner;
    private CourierRoster roster;

    @BeforeEach
    void setUp() {
        this.deliveryRepository = mock(DeliveryRepository.class);
        this.shipmentServicePort = mock(ShipmentServicePort.class);
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        this.assigner = new AssignmentBatchAssigner(deliveryRepository, shipmentServicePort, domainEventPublisher,
                Clock.fixed(Instant.parse("2026-11-24T13:00:00Z"), WARSAW));
        this.roster = new CourierRoster(List.of(KATOWICE_COURIER, SECOND_KATOWICE_COURIER), Map.of());
        when(deliveryRepository.assignCourier(any())).thenReturn(true);
    }

    @Test
    void shouldAssignEligibleDeliveryWithConditionalUpdateAndPublishEvent() {
        final Delivery delivery = awaitingCourierAtDestination("d-1", FIRST);
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of(FIRST, "40-001"));

        final AssignmentBatch batch = assigner.assign(List.of(candidate(delivery)), null, 2, roster);

        assertThat(batch.summary(), equalTo(new AssignmentRunSummary(1, 1, 1, 0, 0, 0, 0, 0, 1)));
        assertThat(delivery.getSupplierId(), equalTo(KATOWICE_COURIER.supplierId()));
        assertThat(batch.maxEligibleAt(), equalTo(ARRIVED_AT.atZone(WARSAW).toInstant()));
        assertThat(batch.lastBatch(), is(true));
        verify(deliveryRepository).assignCourier(delivery);
        verify(domainEventPublisher).publish(any(DeliveryModified.class));
    }

    @Test
    void shouldSkipDeliveryThatIsNotAtDestinationDepartment() {
        final Delivery delivery = inTransit("d-1", FIRST);
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of());

        final AssignmentBatch batch = assigner.assign(List.of(candidate(delivery)), null, 2, roster);

        assertThat(batch.summary().skippedNotAtDepartment(), is(1L));
        assertThat(batch.summary().eligible(), is(0L));
        verify(deliveryRepository, never()).assignCourier(any());
    }

    @Test
    void shouldNotTreatDeliveryWithNonDepotScanAsEligible() {
        final Delivery delivery = lastScannedWith("d-1", FIRST, DeliveryStatus.RETURN);
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of());

        final AssignmentBatch batch = assigner.assign(List.of(candidate(delivery)), null, 2, roster);

        assertThat(batch.summary().discovered(), is(1L));
        assertThat(batch.summary().eligible(), is(0L));
    }

    @Test
    void shouldCountDeliveryWithoutMatchingCourier() {
        final Delivery delivery = awaitingCourierAtDestination("d-1", FIRST);
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of(FIRST, "00-950"));

        final AssignmentBatch batch = assigner.assign(List.of(candidate(delivery)), null, 2, roster);

        assertThat(batch.summary().noCourier(), is(1L));
        assertThat(delivery.getSupplierId(), nullValue());
    }

    @Test
    void shouldCountInvalidDestinationAndContinueBatch() {
        final Delivery withoutPostalCode = awaitingCourierAtDestination("d-1", FIRST);
        final Delivery valid = awaitingCourierAtDestination("d-2", SECOND);
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection()))
                .thenReturn(Map.of(FIRST, " ", SECOND, "40-002"));

        final AssignmentBatch batch = assigner.assign(List.of(candidate(withoutPostalCode), candidate(valid)), null,
                2, roster);

        assertThat(batch.summary().invalidDestination(), is(1L));
        assertThat(batch.summary().assigned(), is(1L));
        assertThat(batch.lastBatch(), is(false));
    }

    @Test
    void shouldCountConcurrentChangeWhenConditionalUpdateDoesNotMatch() {
        final Delivery delivery = awaitingCourierAtDestination("d-1", FIRST);
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of(FIRST, "40-001"));
        when(deliveryRepository.assignCourier(delivery)).thenReturn(false);

        final AssignmentBatch batch = assigner.assign(List.of(candidate(delivery)), null, 2, roster);

        assertThat(batch.summary().skippedConcurrent(), is(1L));
        assertThat(batch.summary().assigned(), is(0L));
        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldNeverOverwriteExistingAssignment() {
        final Delivery delivery = assignedTo("d-1", FIRST, new SupplierId(99L));
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of());

        assigner.assign(List.of(candidate(delivery)), null, 2, roster);

        assertThat(delivery.getSupplierId(), equalTo(new SupplierId(99L)));
        verify(deliveryRepository, never()).assignCourier(any());
    }

    @Test
    void shouldKeepPreviousCursorForEmptyBatch() {
        final DeliveryAssignmentCursor previous = new DeliveryAssignmentCursor(ARRIVED_AT,
                new com.warehouse.commonassets.identificator.DeliveryId("d-9"));
        when(shipmentServicePort.findRecipientPostalCodes(anyCollection())).thenReturn(Map.of());

        final AssignmentBatch batch = assigner.assign(List.of(), previous, 2, roster);

        assertThat(batch.nextCursor(), equalTo(previous));
        assertThat(batch.lastBatch(), is(true));
    }

    private static AwaitingCourierDelivery candidate(final Delivery delivery) {
        return new AwaitingCourierDelivery(delivery, new DeliveryAssignmentCursor(ARRIVED_AT, delivery.getDeliveryId()));
    }
}
