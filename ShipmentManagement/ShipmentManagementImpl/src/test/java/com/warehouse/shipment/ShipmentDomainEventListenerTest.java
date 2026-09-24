package com.warehouse.shipment;

import com.warehouse.shipment.application.listener.ShipmentDomainEventListener;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.event.ShipmentLocked;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ShipmentDomainEventListenerTest {

    @Mock
    private ShipmentPort shipmentPort;

    @Mock
    private PathFinderServicePort pathFinderServicePort;

    private ShipmentDomainEventListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new ShipmentDomainEventListener(this.shipmentPort, this.pathFinderServicePort);
    }

    @Test
    void shouldCreateReturnToSenderShipmentAfterReturnedEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentReturned(shipment.snapshot(), Instant.now()));

        verify(this.shipmentPort).returnToSender(shipment.getShipmentId());
        verifyNoInteractions(this.pathFinderServicePort);
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
}
