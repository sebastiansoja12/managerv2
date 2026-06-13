package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.shipment.application.event.ShipmentReadModelChanged;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "manager.kafka.shipment-read-model-sync.enabled", havingValue = "true")
@Slf4j
public class ShipmentReadModelSyncListener {

    private final ShipmentReadModelSyncPort syncPort;

    public ShipmentReadModelSyncListener(final ShipmentReadModelSyncPort syncPort) {
        this.syncPort = syncPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-read-model-sync:shipment.read-model.sync}",
            groupId = "${manager.kafka.consumer-groups.shipment-read-model-sync:${spring.application.name}-shipment-read-model}"
    )
    public void handle(final ShipmentReadModelChanged event) {
        this.syncPort.syncReadModel(event.snapshot().shipmentId());
        log.info("Synced shipment read model for shipment {}", event.snapshot().shipmentId().getValue());
    }
}
