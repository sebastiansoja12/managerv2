package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.event.ShipmentCreatedEvent;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.notifier.RouteTrackerNotifier;

@Component
public class ShipmentEventListener {

    private final RouteTrackerNotifier routeTrackerNotifier;

    public ShipmentEventListener(final RouteTrackerNotifier routeTrackerNotifier) {
        this.routeTrackerNotifier = routeTrackerNotifier;
    }

    @EventListener
    public void handle(final ShipmentCreatedEvent event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        routeTrackerNotifier.notifyShipmentCreated(snapshot);
    }
}
