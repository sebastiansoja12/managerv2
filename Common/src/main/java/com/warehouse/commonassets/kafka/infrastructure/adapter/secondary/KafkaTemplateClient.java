package com.warehouse.commonassets.kafka.infrastructure.adapter.secondary;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;
import com.warehouse.commonassets.kafka.infrastructure.annotation.KafkaTopic;
import com.warehouse.commonassets.repository.OperatorContextProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaTemplateClient {

    private static final String KAFKA_TYPE_ID = "__TypeId__";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ObjectProvider<OperatorContextProvider> operatorContextProvider;
    private final Environment environment;

    public KafkaTemplateClient(final KafkaTemplate<String, String> kafkaTemplate,
                               final ObjectMapper objectMapper,
                               final ObjectProvider<OperatorContextProvider> operatorContextProvider,
                               final Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.operatorContextProvider = operatorContextProvider;
        this.environment = environment;
    }

    public <T> CompletableFuture<Void> publish(final String key, final T event) {
        return this.publish(key, event, Map.of());
    }

    public <T> CompletableFuture<Void> publish(final String key,
                                               final T event,
                                               final Map<String, String> headers) {
        return this.publish(this.topic(event), key, event, headers);
    }

    public <T> CompletableFuture<Void> publish(final String topic, final String key, final T event) {
        return this.publish(topic, key, event, Map.of());
    }

    public <T> CompletableFuture<Void> publish(final String topic,
                                               final String key,
                                               final T event,
                                               final Map<String, String> headers) {
        this.assignOperatorContext(event);
        return this.publishSerialized(topic, key, this.serialize(event), this.eventHeaders(event, headers));
    }

    private <T> void assignOperatorContext(final T event) {
        if (!(event instanceof final OperatorAwareEvent operatorAwareEvent)) {
            return;
        }

        final OperatorContextProvider provider = this.operatorContextProvider.getIfAvailable();
        if (provider == null) {
            return;
        }

        provider.currentOperatorId().ifPresent(operatorId ->
                provider.currentUserId().ifPresent(userId ->
                        provider.currentDepartmentId().ifPresent(departmentId ->
                                operatorAwareEvent.assignOperatorContext(operatorId, userId, departmentId))));
    }

    private <T> String topic(final T event) {
        Objects.requireNonNull(event, "Kafka event cannot be null");
        final KafkaTopic kafkaTopic = event.getClass().getAnnotation(KafkaTopic.class);
        if (kafkaTopic == null) {
            throw new IllegalArgumentException("Kafka event does not define topic: " + event.getClass().getName());
        }
        return this.environment.resolvePlaceholders(kafkaTopic.value());
    }

    private <T> Map<String, String> eventHeaders(final T event, final Map<String, String> headers) {
        Objects.requireNonNull(headers, "Kafka headers cannot be null");
        final Map<String, String> eventHeaders = new LinkedHashMap<>(headers);
        final String eventType = event.getClass().getSimpleName();
        eventHeaders.putIfAbsent(KAFKA_TYPE_ID, eventType);
        eventHeaders.putIfAbsent(KafkaEventHeaders.EVENT_TYPE, eventType);
        return eventHeaders;
    }

    public CompletableFuture<Void> publishSerialized(final String topic,
                                                      final String key,
                                                      final String payload,
                                                      final Map<String, String> headers) {
        Objects.requireNonNull(topic, "Kafka topic cannot be null");
        Objects.requireNonNull(payload, "Kafka payload cannot be null");

        final ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);
        headers.forEach((name, value) -> record.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));

        return this.kafkaTemplate.send(record)
                .thenAccept(result -> log.info(
                        "Published Kafka event. Topic: {}, key: {}, partition: {}, offset: {}",
                        topic,
                        key,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                ))
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Cannot publish Kafka event. Topic: {}, key: {}", topic, key, exception);
                    }
                });
    }

    private <T> String serialize(final T event) {
        Objects.requireNonNull(event, "Kafka event cannot be null");
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize Kafka event: " + event.getClass().getName(), exception);
        }
    }
}
