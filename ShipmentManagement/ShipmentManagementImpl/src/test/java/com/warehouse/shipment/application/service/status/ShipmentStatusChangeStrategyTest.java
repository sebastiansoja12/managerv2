package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentRerouted;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ShipmentStatusChangeStrategyTest {

    private final ShipmentCreatedStatusChangeStrategy createdStrategy =
            new ShipmentCreatedStatusChangeStrategy();

    private final ShipmentRedirectedStatusChangeStrategy redirectedStrategy =
            new ShipmentRedirectedStatusChangeStrategy();

    private final ShipmentReroutedStatusChangeStrategy reroutedStrategy =
            new ShipmentReroutedStatusChangeStrategy();

    private final ShipmentSentStatusChangeStrategy sentStrategy = new ShipmentSentStatusChangeStrategy();

    private final ShipmentDeliveredStatusChangeStrategy deliveredStrategy =
            new ShipmentDeliveredStatusChangeStrategy();

    private final ShipmentReturnedStatusChangeStrategy returnedStrategy =
            new ShipmentReturnedStatusChangeStrategy();

    private final ShipmentUnchangedStatusChangeStrategy unchangedStrategy =
            new ShipmentUnchangedStatusChangeStrategy();

    private final ShipmentStatusChangeStrategyResolver resolver = new ShipmentStatusChangeStrategyResolver(List.of(
            createdStrategy,
            redirectedStrategy,
            reroutedStrategy,
            sentStrategy,
            deliveredStrategy,
            returnedStrategy,
            unchangedStrategy));

    @Test
    void shouldResolveStrategyForEveryShipmentStatus() {
        final Map<ShipmentStatus, Class<? extends ShipmentStatusChangeStrategy>> expectedStrategies = Map.ofEntries(
                Map.entry(ShipmentStatus.CREATED, ShipmentCreatedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.PREPARED, ShipmentUnchangedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.ACCEPTED, ShipmentUnchangedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.REROUTE, ShipmentReroutedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.SENT, ShipmentSentStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.DELIVERY, ShipmentDeliveredStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.RETURN, ShipmentReturnedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.REDIRECT, ShipmentRedirectedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.CANCELED, ShipmentUnchangedStatusChangeStrategy.class));

        expectedStrategies.forEach((status, strategyType) ->
                assertInstanceOf(strategyType, resolver.resolve(status)));
    }

    @Test
    void shouldApplyRedirectedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentRedirected.class, redirectedStrategy.process(shipment).orElseThrow());

        verify(shipment).notifyRelatedShipmentRedirected(any(ShipmentId.class));
    }

    @Test
    void shouldApplyReroutedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentRerouted.class, reroutedStrategy.process(shipment).orElseThrow());

        verify(shipment).notifyShipmentRerouted();
    }

    @Test
    void shouldApplySentStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentSent.class, sentStrategy.process(shipment).orElseThrow());

        verify(shipment).notifyShipmentSent();
    }

    @Test
    void shouldApplyDeliveredStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentDelivered.class, deliveredStrategy.process(shipment).orElseThrow());

        verify(shipment).notifyShipmentDelivered();
    }

    @Test
    void shouldApplyReturnedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentReturned.class, returnedStrategy.process(shipment).orElseThrow());

        verify(shipment).notifyShipmentReturned();
    }

    @Test
    void shouldRejectCreatedStatusChange() {
        final Shipment shipment = mock(Shipment.class);

        assertThrows(IllegalStateException.class, () -> createdStrategy.process(shipment));
    }

    @Test
    void shouldLeaveShipmentUnchangedForNonTransitionStatus() {
        final Shipment shipment = mock(Shipment.class);

        assertTrue(unchangedStrategy.process(shipment).isEmpty());

        verify(shipment, never()).notifyShipmentRerouted();
        verify(shipment, never()).notifyShipmentSent();
        verify(shipment, never()).notifyShipmentDelivered();
        verify(shipment, never()).notifyShipmentReturned();
        verify(shipment, never()).notifyRelatedShipmentRedirected(any(ShipmentId.class));
    }
}
