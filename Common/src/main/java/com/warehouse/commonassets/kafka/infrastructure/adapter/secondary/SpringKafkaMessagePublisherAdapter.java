package com.warehouse.commonassets.kafka.infrastructure.adapter.secondary;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.domain.port.KafkaMessagePublisherPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SpringKafkaMessagePublisherAdapter implements KafkaMessagePublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SpringKafkaMessagePublisherAdapter(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CompletableFuture<Void> publish(final String topic,
                                           final String key,
                                           final String payload,
                                           final Map<String, String> headers) {
        Objects.requireNonNull(topic, "Kafka topic cannot be null");
        Objects.requireNonNull(payload, "Kafka payload cannot be null");

        final ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);
        headers.forEach((name, value) -> record.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));
        return this.kafkaTemplate
                .send(record)
                .thenAccept(result -> log.info(
                        "Published Kafka message. Topic: {}, key: {}, partition: {}, offset: {}",
                        topic,
                        key,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                ))
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Cannot publish Kafka message. Topic: {}, key: {}", topic, key, exception);
                    }
                });
    }
}
