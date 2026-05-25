package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.shipment.domain.event.ShipmentChangedEvent;
import com.warehouse.shipment.domain.service.ShipmentReadModelSyncService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShipmentReadModelSyncListener {

    private final ShipmentReadModelSyncService syncService;

    public ShipmentReadModelSyncListener(final ShipmentReadModelSyncService syncService) {
        this.syncService = syncService;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-read-model-sync:shipment.read-model.sync}",
            groupId = "${manager.kafka.consumer-groups.shipment-read-model-sync:${spring.application.name}-shipment-read-model}"
    )
    public void handle(final ShipmentChangedEvent event) {
        this.syncService.syncReadModel(event.getSnapshot().shipmentId());
        log.info("Synced shipment read model for shipment {}", event.getSnapshot().shipmentId().getValue());
    }
}
