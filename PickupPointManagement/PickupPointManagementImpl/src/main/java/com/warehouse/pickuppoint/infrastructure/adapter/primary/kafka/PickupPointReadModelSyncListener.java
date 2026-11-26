package com.warehouse.pickuppoint.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.pickuppoint.api.event.PickupPointReadModelChangedIntegrationEvent;
import com.warehouse.pickuppoint.application.port.primary.PickupPointReadModelSyncPort;
import org.springframework.stereotype.Component;

@Component
public class PickupPointReadModelSyncListener {

    private final PickupPointReadModelSyncPort syncPort;

    public PickupPointReadModelSyncListener(final PickupPointReadModelSyncPort syncPort) {
        this.syncPort = syncPort;
    }

    @IntegrationEventListener(group = "manager-pickup-point-read-model")
    public void handle(final PickupPointReadModelChangedIntegrationEvent event) {
        this.syncPort.syncReadModel(new PickupPointId(event.snapshot().pickupPointId().value()));
    }
}
