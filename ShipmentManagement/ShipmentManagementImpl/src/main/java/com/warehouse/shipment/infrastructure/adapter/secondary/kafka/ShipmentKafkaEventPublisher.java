package com.warehouse.shipment.infrastructure.adapter.secondary.kafka;

import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.shipment.domain.event.ShipmentCreatedEvent;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentReturnCreated;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
public class ShipmentKafkaEventPublisher {

    private static final String KAFKA_TYPE_ID = "__TypeId__";
    private static final String SHIPMENT_CREATED = "ShipmentCreated";
    private static final String SHIPMENT_RETURNED = "ShipmentReturned";
    private static final String SHIPMENT_CHANGED = "ShipmentChanged";

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
                this.shipmentEventsTopic,
                event.kafkaKey(),
                event,
                Map.of(
                        KAFKA_TYPE_ID, this.typeId(event),
                        KafkaEventHeaders.EVENT_TYPE, event.getClass().getSimpleName()
                )
        );
    }

    private String typeId(final ShipmentEvent event) {
        if (event instanceof ShipmentCreatedEvent) {
            return SHIPMENT_CREATED;
        }
        if (event instanceof ShipmentReturned || event instanceof ShipmentReturnCreated) {
            return SHIPMENT_RETURNED;
        }
        return SHIPMENT_CHANGED;
    }
}
