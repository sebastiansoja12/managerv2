package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import static com.warehouse.eventoutbox.outbox.OutboxFixture.NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import com.warehouse.eventoutbox.integration.model.EventId;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

class KafkaOutboxDeadLetterAdapterTest {

    @SuppressWarnings("unchecked")
    private final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
    private final KafkaOutboxDeadLetterAdapter adapter = new KafkaOutboxDeadLetterAdapter(this.kafkaTemplate, "outbox.dead");

    @Test
    @SuppressWarnings("unchecked")
    void shouldPublishDeadEventWithOriginalHeadersAndFailureDetails() {
        when(this.kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(new CompletableFuture<>());
        final OutboxEvent dead = deadEvent();

        this.adapter.publish(dead);

        final ArgumentCaptor<ProducerRecord<String, String>> record = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(this.kafkaTemplate).send(record.capture());
        assertThat(record.getValue().topic()).isEqualTo("outbox.dead");
        assertThat(record.getValue().key()).isEqualTo(dead.getEventId().value().toString());
        assertThat(record.getValue().value()).isEqualTo("{\"shipmentId\":1}");
        assertThat(header(record.getValue(), "ce_type")).isEqualTo("shipment.created");
        assertThat(header(record.getValue(), "outbox_original_topic")).isEqualTo("shipment.events");
        assertThat(header(record.getValue(), "outbox_original_key")).isEqualTo("SH-1");
        assertThat(header(record.getValue(), "outbox_attempts")).isEqualTo("10");
        assertThat(header(record.getValue(), "outbox_error")).isEqualTo("TimeoutException");
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotPropagateFailureOfDeadLetterPublication() {
        when(this.kafkaTemplate.send(any(ProducerRecord.class))).thenThrow(new IllegalStateException("broker down"));

        assertThatCode(() -> this.adapter.publish(deadEvent())).doesNotThrowAnyException();
    }

    private String header(final ProducerRecord<String, String> record, final String name) {
        return new String(record.headers().lastHeader(name).value(), StandardCharsets.UTF_8);
    }

    private OutboxEvent deadEvent() {
        return new OutboxEvent(EventId.nextId(), "shipment.created", 1, "shipment.events", "SH-1",
                "{\"shipmentId\":1}", Map.of("ce_type", "shipment.created"), NOW, OutboxEventStatus.DEAD, 10, NOW,
                "TimeoutException", null);
    }
}
