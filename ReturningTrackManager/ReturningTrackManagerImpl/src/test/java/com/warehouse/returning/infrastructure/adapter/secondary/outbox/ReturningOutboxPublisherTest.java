package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReturningOutboxPublisherTest {
    private final ReturningOutboxStore store = mock(ReturningOutboxStore.class);
    @SuppressWarnings("unchecked")
    private final KafkaTemplate<String, String> kafka = mock(KafkaTemplate.class);
    private final ReturningOutboxPublisher publisher = new ReturningOutboxPublisher(
            store, kafka, new ObjectMapper(), 1, 60000, 20, 5000, 10);

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void shouldRetryTheSameEventAndMarkPublishedOnlyAfterKafkaAcknowledgesIt() {
        final ReturningOutboxEntity entity = new ReturningOutboxEntity(UUID.randomUUID(), new ReturnPackageId(123L),
                "return.processing.started", "return.processing.started", "456", "{\"shipmentId\":456}",
                "{\"__TypeId__\":\"ReturnProcessingStartedIntegrationEvent\"}", Instant.now());
        final ReturningOutboxMessage first = entity.claim(Instant.now().plusSeconds(60));
        final ReturningOutboxMessage retry = entity.claim(Instant.now().plusSeconds(120));
        when(store.claim(any(), any())).thenReturn(Optional.of(first), Optional.of(retry));
        when(kafka.send(any(ProducerRecord.class))).thenReturn(
                CompletableFuture.failedFuture(new IllegalStateException("Broker unavailable")),
                CompletableFuture.completedFuture(null));

        publisher.publishPending();

        verify(store, never()).markPublished(any());
        verify(store).markFailed(eq(first), any(), any(), eq(10));

        publisher.publishPending();

        verify(store).markPublished(retry);
        final ArgumentCaptor<ProducerRecord<String, String>> records = ArgumentCaptor.forClass((Class) ProducerRecord.class);
        verify(kafka, times(2)).send(records.capture());
        assertEquals(first.eventId(), retry.eventId());
        assertNotEquals(first.lockToken(), retry.lockToken());
        assertEquals(2, retry.attemptCount());
        assertEquals(records.getAllValues().getFirst().value(), records.getAllValues().getLast().value());
        assertEquals("456", records.getValue().key());
        assertEquals("ReturnProcessingStartedIntegrationEvent", new String(
                records.getValue().headers().lastHeader("__TypeId__").value(), StandardCharsets.UTF_8));
    }

    @Test
    void shouldLeaveUnacknowledgedMessageForRetry() {
        final ReturningOutboxMessage message = new ReturningOutboxMessage(UUID.randomUUID(), "returns", "456",
                "{}", "{}", 1, UUID.randomUUID());
        when(store.claim(any(), any())).thenReturn(Optional.of(message));
        when(kafka.send(any(ProducerRecord.class))).thenReturn(new CompletableFuture<SendResult<String, String>>());

        publisher.publishPending();

        verify(store, never()).markPublished(any());
        verify(store).markFailed(eq(message), any(java.util.concurrent.TimeoutException.class), any(), eq(10));
    }
}
