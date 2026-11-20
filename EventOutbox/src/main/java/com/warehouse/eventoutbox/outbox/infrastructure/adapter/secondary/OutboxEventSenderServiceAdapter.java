package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventSenderServicePort;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxTracing;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboxEventSenderServiceAdapter implements OutboxEventSenderServicePort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxTracing tracing;

    public OutboxEventSenderServiceAdapter(final KafkaTemplate<String, String> kafkaTemplate,
                                           final OutboxTracing tracing) {
        this.kafkaTemplate = kafkaTemplate;
        this.tracing = tracing;
    }

    @Override
    public CompletableFuture<Void> send(final OutboxEvent outboxEvent) {
        final ProducerRecord<String, String> record = new ProducerRecord<>(
                outboxEvent.getTopic(), outboxEvent.getMessageKey(), outboxEvent.getPayload());
        final OutboxTracing.OutboxSendSpan span = this.tracing.startSend(outboxEvent);
        span.headers().forEach((name, value) ->
                record.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));
        final CompletableFuture<Void> sent;
        try {
            sent = this.kafkaTemplate.send(record)
                    .thenAccept(result -> log.debug("[outbox] sent eventId={} topic={} partition={} offset={}",
                            outboxEvent.getEventId().value(), outboxEvent.getTopic(),
                            result.getRecordMetadata().partition(), result.getRecordMetadata().offset()));
        } catch (final RuntimeException exception) {
            span.end(exception);
            throw exception;
        }
        return sent.whenComplete((ignored, error) -> span.end(error));
    }
}
