package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.event.domain.port.IntegrationEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.application.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCanceledIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCreatedIntegrationEvent;
import com.warehouse.shipment.application.listener.ShipmentEventListener;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.event.ShipmentDestinationChanged;
import com.warehouse.shipment.domain.event.ShipmentLocked;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentReturnCanceled;
import com.warehouse.shipment.domain.event.ShipmentReturnCreated;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.exception.DestinationDepartmentDeterminationException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;

@ExtendWith(MockitoExtension.class)
class ShipmentEventListenerTest {

    @Mock
    private ShipmentPort shipmentPort;

    @Mock
    private PathFinderServicePort pathFinderServicePort;

    @Mock
    private IntegrationEventPublisher integrationEventPublisher;

    private ShipmentEventListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new ShipmentEventListener(
                this.shipmentPort,
                this.pathFinderServicePort,
                this.integrationEventPublisher
        );
    }

    @Test
    void shouldPublishCreatedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentCreated(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentCreatedIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishDestinationChangedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentDestinationChanged(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentDestinationChangedIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishReturnCanceledIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentReturnCanceled(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentReturnCanceledIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishReturnCreatedIntegrationEventWithReturnDetails() {
        final Shipment shipment = DataTestCreator.shipment();
        final DepartmentCode departmentCode = new DepartmentCode("KT2");
        final Instant timestamp = Instant.parse("2026-08-31T06:00:00Z");
        final ArgumentCaptor<IntegrationEvent> eventCaptor = ArgumentCaptor.forClass(IntegrationEvent.class);

        this.listener.handle(new ShipmentReturnCreated(
                shipment.snapshot(),
                ReasonCode.DAMAGED,
                "Package damaged",
                departmentCode,
                timestamp
        ));

        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final ShipmentReturnCreatedIntegrationEvent event =
                (ShipmentReturnCreatedIntegrationEvent) eventCaptor.getValue();
        assertEquals(shipment.getShipmentId(), event.getSnapshot().shipmentId());
        assertEquals(timestamp, event.getTimestamp());
        assertEquals(ReasonCode.DAMAGED.name(), event.getReasonCode());
        assertEquals("Package damaged", event.getReason());
        assertEquals(departmentCode, event.getDepartmentCode());
    }

    @Test
    void shouldChangeDestinationWhenReturnedShipmentCanBeRouted() {
        final Shipment shipment = DataTestCreator.shipment();
        final DepartmentCode destination = new DepartmentCode("WA2");
        when(this.pathFinderServicePort.determineDeliveryDepartment(org.mockito.ArgumentMatchers.any(Address.class)))
                .thenReturn(Result.success(new VoronoiResponse(destination)));

        this.listener.handle(new ShipmentReturned(shipment.snapshot(), Instant.now()));

        verify(this.shipmentPort).changeDestination(shipment.getShipmentId(), destination);
    }

    @Test
    void shouldRejectReturnedShipmentWhenDestinationCannotBeDetermined() {
        final Shipment shipment = DataTestCreator.shipment();
        when(this.pathFinderServicePort.determineDeliveryDepartment(org.mockito.ArgumentMatchers.any(Address.class)))
                .thenReturn(Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE));

        assertThrows(
                DestinationDepartmentDeterminationException.class,
                () -> this.listener.handle(new ShipmentReturned(shipment.snapshot(), Instant.now()))
        );
    }

    @Test
    void shouldLockShipmentAfterLockedEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentLocked(shipment.snapshot(), Instant.now()));

        verify(this.shipmentPort).lockShipment(shipment.getShipmentId());
    }

    @Test
    void shouldRedirectShipmentToSenderAfterRedirectedEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentRedirected(shipment.snapshot(), Instant.now()));

        verify(this.shipmentPort).redirectShipmentToSender(shipment.getShipmentId());
    }

    private void assertPublishedEvent(final Class<? extends IntegrationEvent> expectedType,
                                      final Shipment shipment) {
        final ArgumentCaptor<IntegrationEvent> eventCaptor = ArgumentCaptor.forClass(IntegrationEvent.class);
        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final IntegrationEvent event = eventCaptor.getValue();
        assertEquals(expectedType, event.getClass());
        assertEquals(shipment.getShipmentId(),
                ((com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent) event)
                        .payload().shipmentId());
    }
}
