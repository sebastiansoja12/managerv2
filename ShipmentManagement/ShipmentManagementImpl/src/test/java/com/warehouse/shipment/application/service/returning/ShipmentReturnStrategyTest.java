package com.warehouse.shipment.application.service.returning;

import com.warehouse.shipment.application.port.primary.command.ShipmentReturnCommand;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.event.ShipmentLocked;
import com.warehouse.shipment.domain.event.ShipmentReturnCanceled;
import com.warehouse.shipment.domain.event.ShipmentReturnCreated;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ShipmentReturnStrategyTest {

    private final ShipmentReturnCreatedStrategy createdStrategy = new ShipmentReturnCreatedStrategy();

    private final ShipmentReturnCompletedStrategy completedStrategy = new ShipmentReturnCompletedStrategy();

    private final ShipmentReturnCancelledStrategy cancelledStrategy = new ShipmentReturnCancelledStrategy();

    private final ShipmentReturnUnchangedStrategy unchangedStrategy = new ShipmentReturnUnchangedStrategy();

    private final ShipmentReturnStrategyResolver resolver = new ShipmentReturnStrategyResolver(List.of(
            createdStrategy, completedStrategy, cancelledStrategy, unchangedStrategy));

    @Test
    void shouldResolveStrategyForEveryReturnStatus() {
        final Map<ReturnStatus, Class<? extends ShipmentReturnStrategy>> expectedStrategies = Map.ofEntries(
                Map.entry(ReturnStatus.CREATED, ShipmentReturnCreatedStrategy.class),
                Map.entry(ReturnStatus.PROCESSING, ShipmentReturnUnchangedStrategy.class),
                Map.entry(ReturnStatus.COMPLETED, ShipmentReturnCompletedStrategy.class),
                Map.entry(ReturnStatus.CANCELLED, ShipmentReturnCancelledStrategy.class));

        expectedStrategies.forEach((status, strategyType) ->
                assertInstanceOf(strategyType, resolver.resolve(status)));
    }

    @Test
    void shouldApplyCreatedStrategy() {
        final Shipment shipment = mock(Shipment.class);
        final ShipmentReturnCommand command = mock(ShipmentReturnCommand.class);

        assertInstanceOf(ShipmentReturnCreated.class, createdStrategy.process(shipment, command).orElseThrow());

        verify(shipment).notifyShipmentReturned();
    }

    @Test
    void shouldApplyCompletedStrategy() {
        final Shipment shipment = mock(Shipment.class);
        final ShipmentReturnCommand command = mock(ShipmentReturnCommand.class);

        assertInstanceOf(ShipmentLocked.class, completedStrategy.process(shipment, command).orElseThrow());

        verify(shipment).lockShipment();
    }

    @Test
    void shouldApplyCancelledStrategy() {
        final Shipment shipment = mock(Shipment.class);
        final ShipmentReturnCommand command = mock(ShipmentReturnCommand.class);

        assertInstanceOf(ShipmentReturnCanceled.class, cancelledStrategy.process(shipment, command).orElseThrow());

        verify(shipment).notifyShipmentReturnCanceled();
    }

    @Test
    void shouldLeaveShipmentUnchangedForProcessingStatus() {
        final Shipment shipment = mock(Shipment.class);
        final ShipmentReturnCommand command = mock(ShipmentReturnCommand.class);

        assertTrue(unchangedStrategy.process(shipment, command).isEmpty());

        verify(shipment, never()).notifyShipmentReturned();
        verify(shipment, never()).lockShipment();
        verify(shipment, never()).notifyShipmentReturnCanceled();
    }
}
