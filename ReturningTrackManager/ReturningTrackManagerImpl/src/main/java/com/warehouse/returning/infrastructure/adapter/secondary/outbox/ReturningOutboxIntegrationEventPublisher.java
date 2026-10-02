package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.warehouse.returning.application.port.secondary.ReturnEventPublisherServicePort;
import com.warehouse.returning.api.event.ReturnLifecycleIntegrationEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ReturningOutboxIntegrationEventPublisher implements ReturnEventPublisherServicePort {

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
    public void publish(final ReturnLifecycleIntegrationEvent event) {
        final String topic = environment.getRequiredProperty("manager.kafka.integration-events.routes." + event.eventType());
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put("__TypeId__", event.getClass().getName());
        headers.put("eventId", event.eventId().toString());
        headers.put("eventType", event.eventType());
        headers.put("eventVersion", String.valueOf(event.version()));
        headers.put("occurredAt", event.occurredAt().toString());
        headers.put("operatorId", event.operatorId().value().toString());
        headers.put("departmentId", event.departmentId().value().toString());
        if (event.userId() != null) {
            headers.put("userId", event.userId().value().toString());
        }
        try {
            final ObjectNode payload = objectMapper.valueToTree(event);
            payload.put("eventType", event.eventType());
            payload.put("version", event.version());
            repository.saveAndFlush(new ReturningOutboxEntity(event.eventId(), event.returnPackageId(),
                    event.eventType(), topic, event.eventKey(), objectMapper.writeValueAsString(payload),
                    objectMapper.writeValueAsString(headers), event.occurredAt()));
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize return event", exception);
        }
    }
}
