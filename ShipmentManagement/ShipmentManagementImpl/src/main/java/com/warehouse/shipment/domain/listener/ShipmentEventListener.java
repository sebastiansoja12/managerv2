package com.warehouse.shipment.domain.listener;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.TechnicalException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ShipmentEventListener {

    private final ShipmentService shipmentService;

    private final ReturningServicePort returningServicePort;

    private final PathFinderServicePort pathFinderServicePort;

    public ShipmentEventListener(final ShipmentService shipmentService,
                                 final ReturningServicePort returningServicePort,
                                 final PathFinderServicePort pathFinderServicePort) {
        this.shipmentService = shipmentService;
        this.returningServicePort = returningServicePort;
        this.pathFinderServicePort = pathFinderServicePort;
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentReturned event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final Result<VoronoiResponse, ErrorCode> destinationResult = this.pathFinderServicePort
                .determineDeliveryDepartment(Address.from(snapshot.sender()));

        if (destinationResult.isFailure()) {
            throw new TechnicalException(HttpStatusCode.valueOf(destinationResult.getFailure().getCode()),
                    destinationResult.getFailure().getMessage());
        }

        final VoronoiResponse voronoiResponse = destinationResult.getSuccess();
        this.shipmentService.changeDestination(snapshot.shipmentId(), voronoiResponse.getDepartmentCodeResult());
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentReturnCreated event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final ShipmentId shipmentId = snapshot.shipmentId();
        final ReasonCode reasonCode = event.getReasonCode();
        final String reason = event.getReason();
        this.returningServicePort.shipmentReturnCommand(
                new ShipmentReturnedCommand(shipmentId, reasonCode, reason)
        );
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentLocked event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        this.returningServicePort.notifyShipmentReturnCompleted(snapshot);
    }

}
