package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.shipment.application.service.ShipmentReadModelSyncService;
import com.warehouse.shipment.infrastructure.adapter.secondary.kafka.event.ShipmentReadModelChanged;

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
    public void handle(final ShipmentReadModelChanged event) {
        this.syncService.syncReadModel(event.snapshot().shipmentId());
        log.info("Synced shipment read model for shipment {}", event.snapshot().shipmentId().getValue());
    }
}
