package com.warehouse.pickuppoint.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.pickuppoint.api.event.PickupPointReadModelChangedIntegrationEvent;
import com.warehouse.pickuppoint.application.port.primary.PickupPointReadModelSyncPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "manager.kafka.pickup-point-read-model-sync.enabled", havingValue = "true")
public class PickupPointReadModelSyncListener {

    private final PickupPointReadModelSyncPort syncPort;

    public PickupPointReadModelSyncListener(final PickupPointReadModelSyncPort syncPort) {
        this.syncPort = syncPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.pickup-point-read-model-sync:pickup-point.read-model.sync}",
            groupId = "${manager.kafka.consumer-groups.pickup-point-read-model-sync:manager-pickup-point-read-model}"
    )
    public void handle(final PickupPointReadModelChangedIntegrationEvent event) {
        this.syncPort.syncReadModel(new PickupPointId(event.snapshot().pickupPointId().value()));
    }
}
