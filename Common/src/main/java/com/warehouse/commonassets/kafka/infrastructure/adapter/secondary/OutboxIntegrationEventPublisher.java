package com.warehouse.commonassets.kafka.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.application.TransactionalKafkaOutboxWriter;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;

@Component
@ConditionalOnProperty(name = "manager.kafka.outbox.enabled", havingValue = "true")
public class OutboxIntegrationEventPublisher implements IntegrationEventPublisher {

    private static final String ROUTE_PROPERTY_PREFIX = "manager.kafka.integration-events.routes.";

    private final Environment environment;
    private final ObjectMapper objectMapper;
    private final TransactionalKafkaOutboxWriter outboxWriter;
    private final OperatorContextProvider operatorContextProvider;

    public OutboxIntegrationEventPublisher(final Environment environment,
                                           final ObjectMapper objectMapper,
                                           final TransactionalKafkaOutboxWriter outboxWriter,
                                           final OperatorContextProvider operatorContextProvider) {
        this.environment = environment;
        this.objectMapper = objectMapper;
        this.outboxWriter = outboxWriter;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public void publish(final IntegrationEvent event) {
        final IntegrationEventType eventType = event.getClass().getAnnotation(IntegrationEventType.class);
        if (eventType == null) {
            throw new IllegalArgumentException(
                    "Integration event does not define its type: " + event.getClass().getName());
        }

        store(event, topic(eventType), eventType.value(), eventType.version(), Instant.now());
    }

    private void store(final IntegrationEvent event,
                       final String topic,
                       final String eventType,
                       final int eventVersion,
                       final Instant occurredAt) {
        final OperatorDetails context = this.operatorContextProvider.currentContext().orElse(null);
        final OperatorId operatorId = context == null ? null : context.operatorId();
        final UserId userId = context == null ? null : context.userId();
        final DepartmentId departmentId = context == null ? null : context.departmentId();
        assignOperatorContext(event, operatorId, userId, departmentId);

        final UUID eventId = UUID.randomUUID();
        final KafkaOutboxRecord record = new KafkaOutboxRecord(
                eventId,
                topic,
                messageKey(event),
                eventType,
                eventVersion,
                occurredAt,
                operatorId,
                serializeMessage(event, eventId, eventType, eventVersion, occurredAt,
                        operatorId, userId, departmentId),
                headers(event.getClass().getName(), eventId, eventType, eventVersion, occurredAt,
                        operatorId, userId, departmentId)
        );
        this.outboxWriter.append(record);
    }

    private String messageKey(final IntegrationEvent event) {
        if (event instanceof final IntegrationEventKey keyedEvent) {
            return keyedEvent.eventKey();
        }
        return null;
    }

    private void assignOperatorContext(final IntegrationEvent event,
                                       final OperatorId operatorId,
                                       final UserId userId,
                                       final DepartmentId departmentId) {
        if (event instanceof final OperatorAwareEvent operatorAwareEvent) {
            operatorAwareEvent.assignOperatorContext(operatorId, userId, departmentId);
        }
    }

    private Map<String, String> headers(final String typeId,
                                        final UUID eventId,
                                        final String eventType,
                                        final int eventVersion,
                                        final Instant occurredAt,
                                        final OperatorId operatorId,
                                        final UserId userId,
                                        final DepartmentId departmentId) {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(KafkaEventHeaders.TYPE_ID, typeId);
        headers.put(KafkaEventHeaders.EVENT_ID, eventId.toString());
        headers.put(KafkaEventHeaders.EVENT_TYPE, eventType);
        headers.put(KafkaEventHeaders.EVENT_VERSION, String.valueOf(eventVersion));
        headers.put(KafkaEventHeaders.OCCURRED_AT, occurredAt.toString());
        putIdentifier(headers, KafkaEventHeaders.OPERATOR_ID, operatorId == null ? null : operatorId.getValue());
        putIdentifier(headers, KafkaEventHeaders.USER_ID, userId == null ? null : userId.getValue());
        putIdentifier(headers, KafkaEventHeaders.DEPARTMENT_ID,
                departmentId == null ? null : departmentId.getValue());
        return headers;
    }

    private void putIdentifier(final Map<String, String> headers, final String name, final Long value) {
        if (value != null) {
            headers.put(name, String.valueOf(value));
        }
    }

    private String topic(final IntegrationEventType eventType) {
        final String property = ROUTE_PROPERTY_PREFIX + eventType.value();
        final String topic = this.environment.getProperty(property);
        if (topic == null || topic.isBlank()) {
            throw new IllegalStateException("Missing integration event route property: " + property);
        }
        return topic;
    }

    private String serializeMessage(final IntegrationEvent event,
                                    final UUID eventId,
                                    final String eventType,
                                    final int eventVersion,
                                    final Instant occurredAt,
                                    final OperatorId operatorId,
                                    final UserId userId,
                                    final DepartmentId departmentId) {
        try {
            final ObjectNode message = this.objectMapper.valueToTree(event);
            message.put("eventId", eventId.toString());
            message.put("eventType", eventType);
            message.put("version", eventVersion);
            message.put("occurredAt", occurredAt.toString());
            putIdentifier(message, "operatorId", operatorId == null ? null : operatorId.getValue());
            putIdentifier(message, "userId", userId == null ? null : userId.getValue());
            putIdentifier(message, "departmentId", departmentId == null ? null : departmentId.getValue());
            return this.objectMapper.writeValueAsString(message);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize event: " + event.getClass().getName(), exception);
        }
    }

    private void putIdentifier(final ObjectNode message, final String name, final Long value) {
        if (value == null) {
            message.putNull(name);
            return;
        }
        message.putObject(name).put("value", value);
    }
}
