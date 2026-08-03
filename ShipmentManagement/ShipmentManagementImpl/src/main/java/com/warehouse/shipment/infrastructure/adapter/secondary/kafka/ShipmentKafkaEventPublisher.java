package com.warehouse.shipment.infrastructure.adapter.secondary.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.warehouse.commonassets.kafka.domain.model.KafkaEventMessage;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.shipment.domain.event.ShipmentEvent;

@Component
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
public class ShipmentKafkaEventPublisher {

    private final KafkaTemplateClient kafkaTemplateClient;
    private final String shipmentEventsTopic;

    public ShipmentKafkaEventPublisher(final KafkaTemplateClient kafkaTemplateClient,
                                       @Value("${manager.kafka.topics.shipment-events:shipment.events}")
                                       final String shipmentEventsTopic) {
        this.kafkaTemplateClient = kafkaTemplateClient;
        this.shipmentEventsTopic = shipmentEventsTopic;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void publish(final ShipmentEvent event) {
        this.kafkaTemplateClient.publish(
                this.shipmentEventsTopic, event.kafkaKey(), KafkaEventMessage.from(event));
    }
}
