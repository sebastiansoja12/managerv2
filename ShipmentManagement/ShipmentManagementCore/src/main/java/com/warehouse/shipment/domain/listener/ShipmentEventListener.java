package com.warehouse.shipment.domain.listener;

import com.warehouse.shipment.domain.vo.ShipmentHistoryTracker;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.event.ShipmentCreatedEvent;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.notifier.RouteTrackerHistoryNotifier;

import java.time.Instant;

@Component
public class ShipmentEventListener {

    private final RouteTrackerHistoryNotifier routeTrackerHistoryNotifier;
    
    private final ShipmentService shipmentService;

    public ShipmentEventListener(final RouteTrackerHistoryNotifier routeTrackerHistoryNotifier, 
                                 final ShipmentService shipmentService) {
        this.routeTrackerHistoryNotifier = routeTrackerHistoryNotifier;
        this.shipmentService = shipmentService;
    }

    @EventListener
    public void handle(final ShipmentCreatedEvent event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
		final Result<RouteProcess, ShipmentErrorCode> routeProcess = this.shipmentService
				.notifyShipmentCreated(snapshot.shipmentId());
		this.routeTrackerHistoryNotifier.notifyShipmentRoute(new ShipmentHistoryTracker(snapshot.shipmentId(),
				snapshot.shipmentStatus(), routeProcess.isFailure() ? routeProcess.getFailure().getMessage() : null,
				routeProcess.isFailure() ? String.valueOf(routeProcess.getFailure().getCode()) : null,
				routeProcess.isSuccess(), Instant.now(), null));
    }
}
