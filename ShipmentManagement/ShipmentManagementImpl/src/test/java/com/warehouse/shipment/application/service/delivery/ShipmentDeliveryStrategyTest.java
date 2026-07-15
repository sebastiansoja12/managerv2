package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ShipmentDeliveryStrategyTest {

    private final ShipmentDeliveredStrategy deliveredStrategy = new ShipmentDeliveredStrategy();

    private final ShipmentReturnedStrategy returnedStrategy = new ShipmentReturnedStrategy();

    private final ShipmentRedirectedStrategy redirectedStrategy = new ShipmentRedirectedStrategy();

    private final ShipmentSentStrategy sentStrategy = new ShipmentSentStrategy();

    private final ShipmentUnchangedStrategy unchangedStrategy = new ShipmentUnchangedStrategy();

    private final ShipmentDeliveryStrategyResolver resolver = new ShipmentDeliveryStrategyResolver(List.of(
            deliveredStrategy, returnedStrategy, redirectedStrategy, sentStrategy, unchangedStrategy));

    @Test
    void shouldResolveStrategyForEveryDeliveryStatus() {
        final Map<DeliveryStatus, Class<? extends ShipmentDeliveryStrategy>> expectedStrategies = Map.ofEntries(
                Map.entry(DeliveryStatus.DELIVERY, ShipmentDeliveredStrategy.class),
                Map.entry(DeliveryStatus.DEPOT, ShipmentDeliveredStrategy.class),
                Map.entry(DeliveryStatus.DELIVERED, ShipmentDeliveredStrategy.class),
                Map.entry(DeliveryStatus.RETURN, ShipmentReturnedStrategy.class),
                Map.entry(DeliveryStatus.UNAVAILABLE, ShipmentRedirectedStrategy.class),
                Map.entry(DeliveryStatus.REJECTED, ShipmentRedirectedStrategy.class),
                Map.entry(DeliveryStatus.SENDER, ShipmentRedirectedStrategy.class),
                Map.entry(DeliveryStatus.UNKNOWN, ShipmentSentStrategy.class),
                Map.entry(DeliveryStatus.LOST, ShipmentSentStrategy.class),
                Map.entry(DeliveryStatus.CLIENT, ShipmentUnchangedStrategy.class));

        expectedStrategies.forEach((status, strategyType) ->
                assertInstanceOf(strategyType, resolver.resolve(status)));
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
    void shouldApplyRedirectedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentRedirected.class, redirectedStrategy.process(shipment).orElseThrow());

        verify(shipment).notifyRelatedShipmentRedirected(any(ShipmentId.class));
    }

    @Test
    void shouldApplySentStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentSent.class, sentStrategy.process(shipment).orElseThrow());

        verify(shipment).notifyShipmentSent();
    }

    @Test
    void shouldLeaveShipmentUnchangedForClientStatus() {
        final Shipment shipment = mock(Shipment.class);

        assertTrue(unchangedStrategy.process(shipment).isEmpty());

        verify(shipment, never()).notifyShipmentDelivered();
        verify(shipment, never()).notifyShipmentReturned();
        verify(shipment, never()).notifyShipmentSent();
        verify(shipment, never()).notifyRelatedShipmentRedirected(any(ShipmentId.class));
    }
}
