package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentRerouted;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.event.ShipmentStatusChanged;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ShipmentStatusChangeStrategyTest {

    private final ShipmentCreatedStatusChangeStrategy createdStrategy =
            new ShipmentCreatedStatusChangeStrategy();

    private final ShipmentPlannedStatusChangeStrategy plannedStrategy =
            new ShipmentPlannedStatusChangeStrategy();

    private final ShipmentAcceptedStatusChangeStrategy acceptedStrategy =
            new ShipmentAcceptedStatusChangeStrategy();

    private final ShipmentRedirectedStatusChangeStrategy redirectedStrategy =
            new ShipmentRedirectedStatusChangeStrategy();

    private final ShipmentReroutedStatusChangeStrategy reroutedStrategy =
            new ShipmentReroutedStatusChangeStrategy();

    private final ShipmentSentStatusChangeStrategy sentStrategy = new ShipmentSentStatusChangeStrategy();

    private final ShipmentDeliveredStatusChangeStrategy deliveredStrategy =
            new ShipmentDeliveredStatusChangeStrategy();

    private final ShipmentReturnedStatusChangeStrategy returnedStrategy =
            new ShipmentReturnedStatusChangeStrategy();

    private final ShipmentPreparedStatusChangeStrategy preparedStrategy =
            new ShipmentPreparedStatusChangeStrategy();

    private final ShipmentCanceledStatusChangeStrategy canceledStrategy =
            new ShipmentCanceledStatusChangeStrategy();

    private final ShipmentStatusChangeStrategyResolver resolver = new ShipmentStatusChangeStrategyResolver(List.of(
            plannedStrategy,
            createdStrategy,
            acceptedStrategy,
            redirectedStrategy,
            reroutedStrategy,
            sentStrategy,
            deliveredStrategy,
            returnedStrategy,
            preparedStrategy,
            canceledStrategy));

    @Test
    void shouldResolveStrategyForEveryShipmentStatus() {
        final Map<ShipmentStatus, Class<? extends ShipmentStatusChangeStrategy>> expectedStrategies = Map.ofEntries(
                Map.entry(ShipmentStatus.PLANNED, ShipmentPlannedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.CREATED, ShipmentCreatedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.PREPARED, ShipmentPreparedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.ACCEPTED, ShipmentAcceptedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.REROUTE, ShipmentReroutedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.SENT, ShipmentSentStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.DELIVERY, ShipmentDeliveredStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.RETURN, ShipmentReturnedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.REDIRECT, ShipmentRedirectedStatusChangeStrategy.class),
                Map.entry(ShipmentStatus.CANCELED, ShipmentCanceledStatusChangeStrategy.class));

        expectedStrategies.forEach((status, strategyType) ->
                assertInstanceOf(strategyType, resolver.resolve(status)));
    }

    @Test
    void shouldApplyRedirectedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentRedirected.class, redirectedStrategy.process(shipment));

        verify(shipment).notifyRelatedShipmentRedirected(any(ShipmentId.class));
    }

    @Test
    void shouldApplyReroutedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentRerouted.class, reroutedStrategy.process(shipment));

        verify(shipment).notifyShipmentRerouted();
    }

    @Test
    void shouldApplySentStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentSent.class, sentStrategy.process(shipment));

        verify(shipment).notifyShipmentSent();
    }

    @Test
    void shouldApplyDeliveredStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentDelivered.class, deliveredStrategy.process(shipment));

        verify(shipment).notifyShipmentDelivered();
    }

    @Test
    void shouldApplyReturnedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentReturned.class, returnedStrategy.process(shipment));

        verify(shipment).notifyShipmentReturned();
    }

    @Test
    void shouldRejectCreatedStatusChange() {
        final Shipment shipment = mock(Shipment.class);

        assertThrows(IllegalStateException.class, () -> createdStrategy.process(shipment));
    }

    @Test
    void shouldRejectPlannedStatusChange() {
        final Shipment shipment = mock(Shipment.class);

        assertThrows(IllegalStateException.class, () -> plannedStrategy.process(shipment));
    }

    @Test
    void shouldApplyAcceptedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentStatusChanged.class, acceptedStrategy.process(shipment));

        verify(shipment).notifyShipmentAccepted();
    }

    @Test
    void shouldApplyPreparedStrategy() {
        final Shipment shipment = mock(Shipment.class);

        assertInstanceOf(ShipmentStatusChanged.class, preparedStrategy.process(shipment));

        verify(shipment).changeShipmentStatus(ShipmentStatus.PREPARED);
    }

    @Test
    void shouldRejectCanceledStatusChange() {
        final Shipment shipment = mock(Shipment.class);

        assertThrows(IllegalStateException.class, () -> canceledStrategy.process(shipment));
    }
}
