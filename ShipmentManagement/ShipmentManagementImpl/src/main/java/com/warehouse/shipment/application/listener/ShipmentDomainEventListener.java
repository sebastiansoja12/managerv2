package com.warehouse.shipment.application.listener;

import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.event.ShipmentLocked;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.exception.DestinationDepartmentDeterminationException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
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

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentReturned event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final Result<VoronoiResponse, ErrorCode> destinationResult = this.pathFinderServicePort
                .determineDeliveryDepartment(Address.from(snapshot.sender()));

        if (destinationResult.isFailure()) {
            throw new DestinationDepartmentDeterminationException(destinationResult.getFailure());
        }

        final VoronoiResponse voronoiResponse = destinationResult.getSuccess();
        this.shipmentPort.changeDestination(snapshot.shipmentId(), voronoiResponse.getDepartmentCodeResult());
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
