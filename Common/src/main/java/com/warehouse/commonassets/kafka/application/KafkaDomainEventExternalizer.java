package com.warehouse.commonassets.kafka.application;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.domain.annotation.KafkaDomainEvent;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventKey;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import com.warehouse.commonassets.repository.OperatorContextProvider;

@Component
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
public class KafkaDomainEventExternalizer {

    private final Environment environment;
    private final ObjectMapper objectMapper;
    private final KafkaOutboxPort outboxPort;
    private final KafkaOutboxPublicationService outboxPublicationService;
    private final ObjectProvider<OperatorContextProvider> operatorContextProvider;

    public KafkaDomainEventExternalizer(final Environment environment,
                                        final ObjectMapper objectMapper,
                                        final KafkaOutboxPort outboxPort,
                                        final KafkaOutboxPublicationService outboxPublicationService,
                                        final ObjectProvider<OperatorContextProvider> operatorContextProvider) {
        this.environment = environment;
        this.objectMapper = objectMapper;
        this.outboxPort = outboxPort;
        this.outboxPublicationService = outboxPublicationService;
        this.operatorContextProvider = operatorContextProvider;
    }

    @EventListener
    public void publishIfRouted(final Object event) {
        final Set<KafkaDomainEvent> routedEvents = routedEvents(event);
        if (routedEvents.isEmpty()) {
            return;
        }

        routedEvents.stream()
                .map(routedEvent -> toOutboxRecord(routedEvent, event))
                .forEach(this::saveAndPublishAfterCommit);
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

    private KafkaOutboxRecord toOutboxRecord(final KafkaDomainEvent routedEvent, final Object event) {
        final UUID eventId = UUID.randomUUID();
        final String eventType = event.getClass().getName();
        final Instant occurredAt = occurredAt(event);
        final OperatorId operatorId = operatorId(event).orElse(null);
        final Map<String, String> headers = headers(eventId, eventType, routedEvent.version(), occurredAt, operatorId);
        return new KafkaOutboxRecord(
                eventId,
                topic(routedEvent),
                kafkaKey(event),
                eventType,
                routedEvent.version(),
                occurredAt,
                operatorId,
                payload(event),
                headers
        );
    }

    private Map<String, String> headers(final UUID eventId,
                                        final String eventType,
                                        final int eventVersion,
                                        final Instant occurredAt,
                                        final OperatorId operatorId) {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(KafkaEventHeaders.EVENT_ID, eventId.toString());
        headers.put(KafkaEventHeaders.EVENT_TYPE, eventType);
        headers.put(KafkaEventHeaders.EVENT_VERSION, String.valueOf(eventVersion));
        headers.put(KafkaEventHeaders.OCCURRED_AT, occurredAt.toString());
        if (operatorId != null && operatorId.getValue() != null) {
            headers.put(KafkaEventHeaders.OPERATOR_ID, String.valueOf(operatorId.getValue()));
        }
        return headers;
    }

    private String payload(final Object event) {
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize Kafka domain event: " + event.getClass().getName(),
                    exception);
        }
    }

    private Set<KafkaDomainEvent> routedEvents(final Object event) {
        final Set<KafkaDomainEvent> annotations = new LinkedHashSet<>();
        Class<?> type = event.getClass();
        while (type != null && type != Object.class) {
            annotations.addAll(Arrays.asList(type.getDeclaredAnnotationsByType(KafkaDomainEvent.class)));
            type = type.getSuperclass();
        }
        return annotations;
    }

    private String topic(final KafkaDomainEvent routedEvent) {
        if (!routedEvent.topicProperty().isBlank()) {
            return this.environment.getProperty(routedEvent.topicProperty(), routedEvent.topic());
        }
        return this.environment.resolvePlaceholders(routedEvent.topic());
    }

    private String kafkaKey(final Object event) {
        if (event instanceof final KafkaEventKey keyedEvent) {
            return keyedEvent.kafkaKey();
        }
        return operatorId(event)
                .map(OperatorId::getValue)
                .map(String::valueOf)
                .orElse(null);
    }

    private Optional<OperatorId> operatorId(final Object event) {
        if (event instanceof final OperatorAwareEvent operatorAwareEvent && operatorAwareEvent.operatorId() != null) {
            return Optional.of(operatorAwareEvent.operatorId());
        }

        final OperatorContextProvider provider = this.operatorContextProvider.getIfAvailable();
        if (provider == null) {
            return Optional.empty();
        }

        return provider.currentOperatorId();
    }

    private Instant occurredAt(final Object event) {
        return invokeInstantMethod(event, "occurredAt")
                .or(() -> invokeInstantMethod(event, "timestamp"))
                .or(() -> invokeInstantMethod(event, "getTimestamp"))
                .orElseGet(Instant::now);
    }

    private Optional<Instant> invokeInstantMethod(final Object event, final String methodName) {
        try {
            final Method method = event.getClass().getMethod(methodName);
            if (!Instant.class.isAssignableFrom(method.getReturnType())) {
                return Optional.empty();
            }
            return Optional.ofNullable((Instant) method.invoke(event));
        } catch (final ReflectiveOperationException exception) {
            return Optional.empty();
        }
    }
}
