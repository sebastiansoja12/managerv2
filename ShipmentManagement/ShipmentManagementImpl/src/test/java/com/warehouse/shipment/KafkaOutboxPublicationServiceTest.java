package com.warehouse.shipment;

import com.warehouse.commonassets.kafka.application.KafkaOutboxPublicationService;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxStatus;
import com.warehouse.commonassets.kafka.domain.port.KafkaMessagePublisherPort;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
                new KafkaOutboxPublicationService(outboxPort, publisherPort, 50, 30_000, 5_000, 10);
        final KafkaOutboxRecord claimedRecord = new KafkaOutboxRecord(
                record.eventId(), record.topic(), record.messageKey(), record.eventType(), record.eventVersion(),
                record.occurredAt(), record.operatorId(), record.payload(), record.headers(),
                KafkaOutboxStatus.PROCESSING, 0);

        when(outboxPort.claimPending(eq(50), anyString(), any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(claimedRecord));
        when(publisherPort.publish(record.topic(), record.messageKey(), record.payload(), headers))
                .thenReturn(CompletableFuture.completedFuture(null));

        service.publishPending();

        verify(publisherPort).publish(record.topic(), record.messageKey(), record.payload(), headers);
        verify(outboxPort).markPublished(eq(eventId), anyString());
    }

    @Test
    void shouldClaimAfterCommitRecordBeforePublishing() {
        final KafkaOutboxPort outboxPort = mock(KafkaOutboxPort.class);
        final KafkaMessagePublisherPort publisherPort = mock(KafkaMessagePublisherPort.class);
        final KafkaOutboxRecord record = record();
        final KafkaOutboxPublicationService service =
                new KafkaOutboxPublicationService(outboxPort, publisherPort, 50, 30_000, 5_000, 10);
        when(outboxPort.claim(eq(record.eventId()), anyString(), any(Instant.class), any(Instant.class)))
                .thenReturn(true);
        when(publisherPort.publish(record.topic(), record.messageKey(), record.payload(), record.headers()))
                .thenReturn(CompletableFuture.completedFuture(null));

        service.publish(record);

        verify(publisherPort).publish(record.topic(), record.messageKey(), record.payload(), record.headers());
        verify(outboxPort).markPublished(eq(record.eventId()), anyString());
    }

    @Test
    void shouldNotPublishRecordClaimedByAnotherWorker() {
        final KafkaOutboxPort outboxPort = mock(KafkaOutboxPort.class);
        final KafkaMessagePublisherPort publisherPort = mock(KafkaMessagePublisherPort.class);
        final KafkaOutboxRecord record = record();
        final KafkaOutboxPublicationService service =
                new KafkaOutboxPublicationService(outboxPort, publisherPort, 50, 30_000, 5_000, 10);
        when(outboxPort.claim(eq(record.eventId()), anyString(), any(Instant.class), any(Instant.class)))
                .thenReturn(false);

        service.publish(record);

        verifyNoInteractions(publisherPort);
    }

    @Test
    void shouldScheduleRetryAfterPublicationFailure() {
        final KafkaOutboxPort outboxPort = mock(KafkaOutboxPort.class);
        final KafkaMessagePublisherPort publisherPort = mock(KafkaMessagePublisherPort.class);
        final KafkaOutboxRecord record = record();
        final RuntimeException failure = new RuntimeException("Kafka unavailable");
        final KafkaOutboxPublicationService service =
                new KafkaOutboxPublicationService(outboxPort, publisherPort, 50, 30_000, 5_000, 10);
        when(outboxPort.claim(eq(record.eventId()), anyString(), any(Instant.class), any(Instant.class)))
                .thenReturn(true);
        when(publisherPort.publish(record.topic(), record.messageKey(), record.payload(), record.headers()))
                .thenReturn(CompletableFuture.failedFuture(failure));

        service.publish(record);

        verify(outboxPort).markFailed(
                eq(record.eventId()), anyString(), eq(failure), any(Instant.class), eq(10));
        verify(outboxPort, never()).markPublished(any(), anyString());
    }

    @Test
    void shouldReleaseClaimWhenPublisherFailsSynchronously() {
        final KafkaOutboxPort outboxPort = mock(KafkaOutboxPort.class);
        final KafkaMessagePublisherPort publisherPort = mock(KafkaMessagePublisherPort.class);
        final KafkaOutboxRecord record = record();
        final RuntimeException failure = new RuntimeException("Producer closed");
        final KafkaOutboxPublicationService service =
                new KafkaOutboxPublicationService(outboxPort, publisherPort, 50, 30_000, 5_000, 10);
        when(outboxPort.claim(eq(record.eventId()), anyString(), any(Instant.class), any(Instant.class)))
                .thenReturn(true);
        when(publisherPort.publish(record.topic(), record.messageKey(), record.payload(), record.headers()))
                .thenThrow(failure);

        service.publish(record);

        verify(outboxPort).markFailed(
                eq(record.eventId()), anyString(), eq(failure), any(Instant.class), eq(10));
    }

    private KafkaOutboxRecord record() {
        final UUID eventId = UUID.randomUUID();
        return new KafkaOutboxRecord(
                eventId,
                "shipment.events",
                "shipment-1",
                "shipment.created",
                1,
                Instant.parse("2026-01-01T00:00:00Z"),
                null,
                "{\"eventId\":\"" + eventId + "\"}",
                Map.of("eventType", "shipment.created")
        );
    }
}
