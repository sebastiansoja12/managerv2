package com.warehouse.shipment;

import com.warehouse.commonassets.kafka.application.KafkaOutboxPublicationService;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaMessagePublisherPort;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KafkaOutboxPublicationServiceTest {

    @Test
    void shouldPublishPendingRecordAndMarkItAsPublished() {
        final KafkaOutboxPort outboxPort = mock(KafkaOutboxPort.class);
        final KafkaMessagePublisherPort publisherPort = mock(KafkaMessagePublisherPort.class);
        final UUID eventId = UUID.randomUUID();
        final Map<String, String> headers = Map.of("eventType", "shipment.created");
        final KafkaOutboxRecord record = new KafkaOutboxRecord(
                eventId,
                "shipment.events",
                "shipment-1",
                "shipment.created",
                1,
                Instant.parse("2026-01-01T00:00:00Z"),
                null,
                "{\"eventId\":\"" + eventId + "\"}",
                headers
        );
        final KafkaOutboxPublicationService service =
                new KafkaOutboxPublicationService(outboxPort, publisherPort, 50);

        when(outboxPort.findUnpublished(50)).thenReturn(List.of(record));
        when(publisherPort.publish(record.topic(), record.messageKey(), record.payload(), headers))
                .thenReturn(CompletableFuture.completedFuture(null));

        service.publishPending();

        verify(publisherPort).publish(record.topic(), record.messageKey(), record.payload(), headers);
        verify(outboxPort).markPublished(eventId);
    }
}
