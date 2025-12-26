package com.warehouse.shipment.domain.listener;

import java.time.Instant;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.warehouse.shipment.domain.event.ShipmentCreatedEvent;
import com.warehouse.shipment.domain.event.ShipmentRecipientChanged;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.service.RouteTrackerService;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.ShipmentHistoryTracker;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.notifier.RouteTrackerHistoryNotifier;

@Component
public class ShipmentEventListener {

    private final RouteTrackerHistoryNotifier routeTrackerHistoryNotifier;
    
    private final ShipmentService shipmentService;

    private final RouteTrackerService routeTrackerService;

    private final ReturningServicePort returningServicePort;

    public ShipmentEventListener(final RouteTrackerHistoryNotifier routeTrackerHistoryNotifier,
                                 final ShipmentService shipmentService,
                                 final RouteTrackerService routeTrackerService,
                                 final ReturningServicePort returningServicePort) {
        this.routeTrackerHistoryNotifier = routeTrackerHistoryNotifier;
        this.shipmentService = shipmentService;
        this.routeTrackerService = routeTrackerService;
        this.returningServicePort = returningServicePort;
    }

    @EventListener
    public void handle(final ShipmentCreatedEvent event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();

        final Result<RouteProcess, ErrorCode> routeProcess = this.routeTrackerService
                .notifyShipmentCreated(snapshot.shipmentId());

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
        final Result<RouteProcess, ErrorCode> routeProcess =
                this.routeTrackerService.notifyShipmentStatusChanged(snapshot.shipmentId(), snapshot.shipmentStatus());
        this.routeTrackerHistoryNotifier.notifyShipmentRoute(new ShipmentHistoryTracker(snapshot.shipmentId(),
                snapshot.shipmentStatus(), null, null, true, Instant.now(), null));

        this.returningServicePort.notifyShipmentReturn(snapshot);
    }

    @EventListener
    public void handle(final ShipmentRecipientChanged event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final Result<RouteProcess, ErrorCode> routeProcess =
                this.routeTrackerService.notifyShipmentPersonChanged(snapshot.shipmentId(), snapshot.recipient());
        this.routeTrackerHistoryNotifier.notifyShipmentRoute(new ShipmentHistoryTracker(snapshot.shipmentId(),
                snapshot.shipmentStatus(), null, null, true, Instant.now(), null));
    }
}
