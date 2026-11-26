package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.core.KafkaTemplate;

import com.warehouse.eventoutbox.kafka.KafkaEventHeaders;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxDeadLetterPort;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaOutboxDeadLetterAdapter implements OutboxDeadLetterPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaOutboxDeadLetterAdapter(final KafkaTemplate<String, String> kafkaTemplate, final String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(final OutboxEvent deadEvent) {
        final ProducerRecord<String, String> record = new ProducerRecord<>(
                this.topic, deadEvent.getEventId().value().toString(), deadEvent.getPayload());
        final Headers headers = record.headers();
        deadEvent.getHeaders().forEach((name, value) -> add(headers, name, value));
        add(headers, KafkaEventHeaders.OUTBOX_ORIGINAL_TOPIC, deadEvent.getTopic());
        add(headers, KafkaEventHeaders.OUTBOX_ORIGINAL_KEY, deadEvent.getMessageKey());
        add(headers, KafkaEventHeaders.OUTBOX_ATTEMPTS, String.valueOf(deadEvent.getAttemptCount()));
        add(headers, KafkaEventHeaders.OUTBOX_ERROR, deadEvent.getLastError());
        try {
            this.kafkaTemplate.send(record).whenComplete((result, error) -> {
                if (error != null) {
                    log.warn("[outbox] dead letter not published eventId={} topic={}",
                            deadEvent.getEventId().value(), this.topic, error);
                }
            });
        } catch (final RuntimeException exception) {
            log.warn("[outbox] dead letter not published eventId={} topic={}",
                    deadEvent.getEventId().value(), this.topic, exception);
        }
    }

    private void add(final Headers headers, final String name, final String value) {
        if (value != null) {
            headers.add(name, value.getBytes(StandardCharsets.UTF_8));
        }
    }
}
