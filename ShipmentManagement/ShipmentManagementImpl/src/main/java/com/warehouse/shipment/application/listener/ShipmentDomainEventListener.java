package com.warehouse.shipment.application.listener;

import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.event.ShipmentLocked;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.event.ShipmentReturnedCompleted;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ShipmentDomainEventListener {

    private final ShipmentPort shipmentPort;
    private final PathFinderServicePort pathFinderServicePort;

    public ShipmentDomainEventListener(final ShipmentPort shipmentPort,
                                       final PathFinderServicePort pathFinderServicePort) {
        this.shipmentPort = shipmentPort;
        this.pathFinderServicePort = pathFinderServicePort;
    }

    @EventListener
    public void handle(final ShipmentReturned event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        this.shipmentPort.returnToSender(snapshot.shipmentId());
    }

    @EventListener
    public void handle(final ShipmentReturnedCompleted event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(
                snapshot.shipmentRelatedId(), snapshot.shipmentStatus()
        );
        this.shipmentPort.changeShipmentStatusTo(request);
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentLocked event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        this.shipmentPort.lockShipment(snapshot.shipmentId());
    }

    @EventListener
    public void handle(final ShipmentRedirected event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        this.shipmentPort.redirectShipmentToSender(snapshot.shipmentId());
    }
}
