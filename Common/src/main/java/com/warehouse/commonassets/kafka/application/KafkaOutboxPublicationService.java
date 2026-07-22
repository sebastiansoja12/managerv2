package com.warehouse.commonassets.kafka.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaMessagePublisherPort;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
public class KafkaOutboxPublicationService {

    private final KafkaOutboxPort outboxPort;
    private final KafkaMessagePublisherPort messagePublisherPort;
    private final int batchSize;

    public KafkaOutboxPublicationService(final KafkaOutboxPort outboxPort,
                                         final KafkaMessagePublisherPort messagePublisherPort,
                                         @Value("${manager.kafka.outbox.batch-size:50}") final int batchSize) {
        this.outboxPort = outboxPort;
        this.messagePublisherPort = messagePublisherPort;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${manager.kafka.outbox.publish-delay-ms:5000}")
    public void publishPending() {
        final List<KafkaOutboxRecord> records = this.outboxPort.findUnpublished(this.batchSize);
        records.forEach(this::publish);
    }

    public void publish(final KafkaOutboxRecord record) {
        this.messagePublisherPort.publish(record.topic(), record.messageKey(), record.payload(), record.headers())
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        this.outboxPort.markFailed(record.eventId(), exception);
                        log.error("Cannot publish outbox Kafka event {}", record.eventId(), exception);
                        return;
                    }

                    this.outboxPort.markPublished(record.eventId());
                });
    }
}
