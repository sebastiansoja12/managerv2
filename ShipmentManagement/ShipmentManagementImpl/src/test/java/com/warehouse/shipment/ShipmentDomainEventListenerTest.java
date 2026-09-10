package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.application.listener.ShipmentDomainEventListener;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.event.ShipmentLocked;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.exception.DestinationDepartmentDeterminationException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void shouldChangeDestinationWhenReturnedShipmentCanBeRouted() {
        final Shipment shipment = DataTestCreator.shipment();
        final DepartmentCode destination = new DepartmentCode("WA2");
        when(this.pathFinderServicePort.determineDeliveryDepartment(any(Address.class)))
                .thenReturn(Result.success(new VoronoiResponse(destination)));

        this.listener.handle(new ShipmentReturned(shipment.snapshot(), Instant.now()));

        verify(this.shipmentPort).changeDestination(shipment.getShipmentId(), destination);
    }

    @Test
    void shouldRejectReturnedShipmentWhenDestinationCannotBeDetermined() {
        final Shipment shipment = DataTestCreator.shipment();
        when(this.pathFinderServicePort.determineDeliveryDepartment(any(Address.class)))
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
}
