package com.warehouse.commonassets.kafka.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
public class IntegrationEventOutboxListener {

    private static final String ROUTE_PROPERTY_PREFIX = "manager.kafka.integration-events.routes.";
    private static final String DOMAIN_EVENT_TOPIC_PROPERTY = "manager.kafka.domain-events.topic";
    private static final int DOMAIN_EVENT_VERSION = 1;

    private final Environment environment;
    private final ObjectMapper objectMapper;
    private final KafkaOutboxPort outboxPort;
    private final KafkaOutboxPublicationService outboxPublicationService;
    private final OperatorContextProvider operatorContextProvider;

    public IntegrationEventOutboxListener(final Environment environment,
                                          final ObjectMapper objectMapper,
                                          final KafkaOutboxPort outboxPort,
                                          final KafkaOutboxPublicationService outboxPublicationService,
                                          final OperatorContextProvider operatorContextProvider) {
        this.environment = environment;
        this.objectMapper = objectMapper;
        this.outboxPort = outboxPort;
        this.outboxPublicationService = outboxPublicationService;
        this.operatorContextProvider = operatorContextProvider;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, fallbackExecution = true)
    public void handle(final IntegrationEvent event) {
        final IntegrationEventType eventType = event.getClass().getAnnotation(IntegrationEventType.class);
        if (eventType == null) {
            throw new IllegalArgumentException(
                    "Integration event does not define its type: " + event.getClass().getName());
        }

        store(event, topic(eventType), eventType.value(), null, eventType.version(), Instant.now());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, fallbackExecution = true)
    public void handle(final DomainEvent event) {
        final Class<?> eventClass = event.getClass();
        store(event, domainEventTopic(), eventClass.getSimpleName(), eventClass.getName(),
                DOMAIN_EVENT_VERSION, event.getTimestamp());
    }

    private void store(final Object event,
                       final String topic,
                       final String eventType,
                       final String eventClass,
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
                null,
                eventClass == null ? eventType : eventClass,
                eventVersion,
                occurredAt,
                operatorId,
                serializeMessage(event, eventId, eventType, eventClass, eventVersion, occurredAt,
                        operatorId, userId, departmentId),
                headers(event.getClass().getSimpleName(), eventId, eventType, eventClass, eventVersion, occurredAt,
                        operatorId, userId, departmentId)
        );
        saveAndPublishAfterCommit(record);
    }

    private void assignOperatorContext(final Object event,
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
                                        final String eventClass,
                                        final int eventVersion,
                                        final Instant occurredAt,
                                        final OperatorId operatorId,
                                        final UserId userId,
                                        final DepartmentId departmentId) {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(KafkaEventHeaders.TYPE_ID, typeId);
        headers.put(KafkaEventHeaders.EVENT_ID, eventId.toString());
        headers.put(KafkaEventHeaders.EVENT_TYPE, eventType);
        if (eventClass != null) {
            headers.put(KafkaEventHeaders.EVENT_CLASS, eventClass);
        }
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

    private String domainEventTopic() {
        final String topic = this.environment.getProperty(DOMAIN_EVENT_TOPIC_PROPERTY);
        if (topic == null || topic.isBlank()) {
            throw new IllegalStateException("Missing domain event topic property: " + DOMAIN_EVENT_TOPIC_PROPERTY);
        }
        return topic;
    }

    private String serializeMessage(final Object event,
                                    final UUID eventId,
                                    final String eventType,
                                    final String eventClass,
                                    final int eventVersion,
                                    final Instant occurredAt,
                                    final OperatorId operatorId,
                                    final UserId userId,
                                    final DepartmentId departmentId) {
        try {
            final ObjectNode message = this.objectMapper.valueToTree(event);
            message.put("eventId", eventId.toString());
            message.put("eventType", eventType);
            if (eventClass != null) {
                message.put("eventClass", eventClass);
            }
            message.put("version", eventVersion);
            message.put("occurredAt", occurredAt.toString());
            putIdentifier(message, "operatorId", operatorId == null ? null : operatorId.getValue());
            putIdentifier(message, "userId", userId == null ? null : userId.getValue());
            putIdentifier(message, "departmentId", departmentId == null ? null : departmentId.getValue());
            return this.objectMapper.writeValueAsString(message);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize event: " + event.getClass().getName(),
                    exception);
        }
    }

    private void putIdentifier(final ObjectNode message, final String name, final Long value) {
        if (value == null) {
            message.putNull(name);
            return;
        }
        message.putObject(name).put("value", value);
    }

    private void saveAndPublishAfterCommit(final KafkaOutboxRecord record) {
        this.outboxPort.save(record);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    outboxPublicationService.publish(record);
                }
            });
            return;
        }
        this.outboxPublicationService.publish(record);
    }
}
