package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.returning.api.event.ReturnLifecycleIntegrationEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ReturningOutboxIntegrationEventPublisher implements IntegrationEventPublisher {

    private final ReturningOutboxRepository repository;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    public ReturningOutboxIntegrationEventPublisher(final ReturningOutboxRepository repository,
                                                    final ObjectMapper objectMapper,
                                                    final Environment environment) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.environment = environment;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void publish(final IntegrationEvent event) {
        if (!(event instanceof final ReturnLifecycleIntegrationEvent returningEvent)) {
            throw new IllegalArgumentException("Unsupported RTM integration event: " + event.getClass().getName());
        }
        final IntegrationEventType eventType = event.getClass().getAnnotation(IntegrationEventType.class);
        final String topic = environment.getRequiredProperty("manager.kafka.integration-events.routes." + eventType.value());
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(KafkaEventHeaders.TYPE_ID, event.getClass().getName());
        headers.put(KafkaEventHeaders.EVENT_ID, returningEvent.eventId().toString());
        headers.put(KafkaEventHeaders.EVENT_TYPE, eventType.value());
        headers.put(KafkaEventHeaders.EVENT_VERSION, String.valueOf(eventType.version()));
        headers.put(KafkaEventHeaders.OCCURRED_AT, returningEvent.occurredAt().toString());
        headers.put(KafkaEventHeaders.OPERATOR_ID, returningEvent.operatorId().value().toString());
        headers.put(KafkaEventHeaders.DEPARTMENT_ID, returningEvent.departmentId().value().toString());
        if (returningEvent.userId() != null) {
            headers.put(KafkaEventHeaders.USER_ID, returningEvent.userId().value().toString());
        }
        try {
            final ObjectNode payload = objectMapper.valueToTree(returningEvent);
            payload.put("eventType", eventType.value());
            payload.put("version", eventType.version());
            repository.saveAndFlush(new ReturningOutboxEntity(returningEvent.eventId(), returningEvent.returnPackageId(),
                    eventType.value(), topic, returningEvent.eventKey(), objectMapper.writeValueAsString(payload),
                    objectMapper.writeValueAsString(headers), returningEvent.occurredAt()));
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize return event", exception);
        }
    }
}
