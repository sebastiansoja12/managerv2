package com.warehouse.commonassets.kafka.application;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import com.warehouse.commonassets.repository.OperatorContextProvider;

@Component
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
public class IntegrationEventOutboxWriter {

    private final ObjectMapper objectMapper;
    private final KafkaOutboxPort outboxPort;
    private final ObjectProvider<OperatorContextProvider> operatorContextProvider;

    public IntegrationEventOutboxWriter(final ObjectMapper objectMapper,
                                        final KafkaOutboxPort outboxPort,
                                        final ObjectProvider<OperatorContextProvider> operatorContextProvider) {
        this.objectMapper = objectMapper;
        this.outboxPort = outboxPort;
        this.operatorContextProvider = operatorContextProvider;
    }

    public <T> void write(final String topic,
                          final String key,
                          final UUID eventId,
                          final String eventType,
                          final int version,
                          final Instant occurredAt,
                          final T event) {
        final OperatorContextProvider provider = this.operatorContextProvider.getIfAvailable();
        assignOperatorContext(event, provider);
        final OperatorId operatorId = operatorId(event, provider);
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(KafkaEventHeaders.EVENT_ID, eventId.toString());
        headers.put(KafkaEventHeaders.EVENT_TYPE, eventType);
        headers.put(KafkaEventHeaders.EVENT_VERSION, String.valueOf(version));
        headers.put(KafkaEventHeaders.OCCURRED_AT, occurredAt.toString());
        if (operatorId != null) {
            headers.put(KafkaEventHeaders.OPERATOR_ID, String.valueOf(operatorId.getValue()));
        }
        this.outboxPort.save(new KafkaOutboxRecord(
                eventId,
                topic,
                key,
                eventType,
                version,
                occurredAt,
                operatorId,
                serialize(event),
                headers
        ));
    }

    private <T> void assignOperatorContext(final T event, final OperatorContextProvider provider) {
        if (!(event instanceof final OperatorAwareEvent operatorAwareEvent) || provider == null) {
            return;
        }
        operatorAwareEvent.assignOperatorContext(
                provider.currentOperatorId().orElse(null),
                provider.currentUserId().orElse(null),
                provider.currentDepartmentId().orElse(null)
        );
    }

    private <T> OperatorId operatorId(final T event, final OperatorContextProvider provider) {
        if (event instanceof final OperatorAwareEvent operatorAwareEvent && operatorAwareEvent.operatorId() != null) {
            return operatorAwareEvent.operatorId();
        }
        return provider == null ? null : provider.currentOperatorId().orElse(null);
    }

    private <T> String serialize(final T event) {
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize integration event: " + event.getClass().getName(),
                    exception);
        }
    }
}
