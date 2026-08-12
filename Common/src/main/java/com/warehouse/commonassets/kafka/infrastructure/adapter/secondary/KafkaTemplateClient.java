package com.warehouse.commonassets.kafka.infrastructure.adapter.secondary;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;
import com.warehouse.commonassets.repository.OperatorContextProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaTemplateClient {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ObjectProvider<OperatorContextProvider> operatorContextProvider;

    public KafkaTemplateClient(final KafkaTemplate<String, String> kafkaTemplate,
                               final ObjectMapper objectMapper,
                               final ObjectProvider<OperatorContextProvider> operatorContextProvider) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.operatorContextProvider = operatorContextProvider;
    }

    public <T> CompletableFuture<Void> publish(final String topic, final String key, final T event) {
        return this.publish(topic, key, event, Map.of());
    }

    public <T> CompletableFuture<Void> publish(final String topic,
                                               final String key,
                                               final T event,
                                               final Map<String, String> headers) {
        this.assignOperatorContext(event);
        return this.publishSerialized(topic, key, this.serialize(event), headers);
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
