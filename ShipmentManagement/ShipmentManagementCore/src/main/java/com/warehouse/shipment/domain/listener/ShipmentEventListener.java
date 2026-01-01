package com.warehouse.shipment.domain.listener;

import java.time.Instant;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.service.RouteTrackerService;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.shipment.infrastructure.adapter.secondary.notifier.RouteTrackerHistoryNotifier;

@Component
public class ShipmentEventListener {

    private final RouteTrackerHistoryNotifier routeTrackerHistoryNotifier;
    
    private final ShipmentService shipmentService;

    private final RouteTrackerService routeTrackerService;

    private final ReturningServicePort returningServicePort;

    private final PathFinderServicePort pathFinderServicePort;

    public ShipmentEventListener(final RouteTrackerHistoryNotifier routeTrackerHistoryNotifier,
                                 final ShipmentService shipmentService,
                                 final RouteTrackerService routeTrackerService,
                                 final ReturningServicePort returningServicePort,
                                 final PathFinderServicePort pathFinderServicePort) {
        this.routeTrackerHistoryNotifier = routeTrackerHistoryNotifier;
        this.shipmentService = shipmentService;
        this.routeTrackerService = routeTrackerService;
        this.returningServicePort = returningServicePort;
        this.pathFinderServicePort = pathFinderServicePort;
    }

    @EventListener
    public void handle(final ShipmentCreatedEvent event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();

        final Result<RouteProcess, ErrorCode> routeProcess = this.routeTrackerService
                .notifyShipmentCreated(snapshot.shipmentId());

        if (routeProcess.isFailure()) {
            throw new TechnicalException(HttpStatusCode.valueOf(routeProcess.getFailure().getCode()),
                    routeProcess.getFailure().getMessage());
        }

        this.shipmentService.changeRouteProcessId(routeProcess.getSuccess().getProcessId(),
                snapshot.shipmentId());

		this.routeTrackerHistoryNotifier.notifyShipmentRoute(new ShipmentHistoryTracker(snapshot.shipmentId(),
				snapshot.shipmentStatus(), routeProcess.isFailure() ? routeProcess.getFailure().getMessage() : null,
				routeProcess.isFailure() ? String.valueOf(routeProcess.getFailure().getCode()) : null,
				routeProcess.isSuccess(), Instant.now(), null));
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
        this.shipmentService.changeDestination(snapshot.shipmentId(), voronoiResponse.getValue());

        final Result<RouteProcess, ErrorCode> result =
                this.routeTrackerService.notifyShipmentStatusChanged(snapshot.shipmentId(), snapshot.shipmentStatus());

        if (result.isFailure()) {
            throw new TechnicalException(HttpStatusCode.valueOf(result.getFailure().getCode()),
                    result.getFailure().getMessage());
        }
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentReturnCreated event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final ShipmentId shipmentId = snapshot.shipmentId();
        final ReasonCode reasonCode = event.getReasonCode();
        final String reason = event.getReason();
        final Map<ShipmentId, ReturnId> responseMap =
                this.returningServicePort.shipmentReturnCommand(
                        new ShipmentReturnedCommand(shipmentId, reasonCode, reason)
                );

        final ReturnId returnId = responseMap.get(shipmentId);

        if (returnId != null) {
            this.shipmentService.assignExternalReturnId(shipmentId, returnId);
        }
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentLocked event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        this.returningServicePort.notifyShipmentReturnCompleted(snapshot);
    }

    @EventListener
    public void handle(final ShipmentRecipientChanged event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final Result<RouteProcess, ErrorCode> routeProcess =
                this.routeTrackerService.notifyShipmentPersonChanged(snapshot.shipmentId(), snapshot.recipient());
        this.routeTrackerHistoryNotifier.notifyShipmentRoute(new ShipmentHistoryTracker(snapshot.shipmentId(),
                snapshot.shipmentStatus(), null, null, true, Instant.now(), null));
    }

    @EventListener
    public void handle(final ShipmentRedirected event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final Result<RouteProcess, ErrorCode> routeProcess = this.routeTrackerService
                .notifyShipmentStatusChanged(snapshot.shipmentId(), snapshot.shipmentStatus());

        if (routeProcess.isFailure()) {
            throw new TechnicalException(HttpStatusCode.valueOf(routeProcess.getFailure().getCode()),
                    routeProcess.getFailure().getMessage());
        }
    }
}
