package com.warehouse.commonassets.kafkapublisher;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaPublisher(
            final KafkaTemplate<String, String> kafkaTemplate,
            final ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public <T extends KeyedKafkaMessage> CompletableFuture<SendResult<String, String>> publish(
            final String topic,
            final T message
    ) {
        return publish(topic, message.kafkaKey(), message);
    }

    public <T> CompletableFuture<SendResult<String, String>> publish(
            final String topic,
            final String key,
            final T message
    ) {
        Objects.requireNonNull(topic, "Kafka topic cannot be null");
        Objects.requireNonNull(message, "Kafka message cannot be null");

        final String payload = serialize(message);

        return this.kafkaTemplate
                .send(topic, key, payload)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error(
                                "Cannot publish Kafka message. Topic: {}, key: {}",
                                topic,
                                key,
                                exception
                        );
                        return;
                    }

                    log.info(
                            "Published Kafka message. Topic: {}, key: {}, partition: {}, offset: {}",
                            topic,
                            key,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset()
                    );
                });
    }

    private <T> String serialize(final T message) {
        try {
            return this.objectMapper.writeValueAsString(message);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Cannot serialize Kafka message of type: " + message.getClass().getName(),
                    exception
            );
        }
    }
}