package com.warehouse.shipment.infrastructure.adapter.secondary.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.shipment.domain.event.ShipmentChanged;
import com.warehouse.shipment.infrastructure.adapter.secondary.kafka.event.ShipmentReadModelChanged;

@Component
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
public class ShipmentReadModelSyncKafkaPublisher {

    private final KafkaTemplateClient kafkaTemplateClient;

    public ShipmentReadModelSyncKafkaPublisher(final KafkaTemplateClient kafkaTemplateClient) {
        this.kafkaTemplateClient = kafkaTemplateClient;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void publish(final ShipmentChanged event) {
        final ShipmentReadModelChanged readModelEvent = new ShipmentReadModelChanged(
                event.getSnapshot(),
                event.getTimestamp()
        );
        this.kafkaTemplateClient.publish(String.valueOf(event.getSnapshot().shipmentId().getValue()), readModelEvent);
    }
}
