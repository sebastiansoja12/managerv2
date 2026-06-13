package com.warehouse.shipment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.application.IntegrationEventOutboxListener;
import com.warehouse.commonassets.kafka.application.KafkaOutboxPublicationService;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IntegrationEventOutboxListenerTest {

    @Test
    @SuppressWarnings("unchecked")
    void shouldStoreBusinessPayloadAndTechnicalMetadataSeparately() throws Exception {
        final Environment environment = mock(Environment.class);
        final KafkaOutboxPort outboxPort = mock(KafkaOutboxPort.class);
        final KafkaOutboxPublicationService publicationService = mock(KafkaOutboxPublicationService.class);
        final OperatorContextProvider contextProvider = mock(OperatorContextProvider.class);
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        when(environment.getProperty("manager.kafka.integration-events.routes.shipment.changed"))
                .thenReturn("shipment.events");
        when(contextProvider.currentContext()).thenReturn(Optional.of(new OperatorDetails(
                OperatorId.of(7L), new UserId(42L), new DepartmentId(10L))));
        final IntegrationEventOutboxListener listener = new IntegrationEventOutboxListener(
                environment, objectMapper, outboxPort, publicationService, contextProvider);
        final ShipmentChangedIntegrationEvent event = new ShipmentChangedIntegrationEvent(
                ShipmentEventData.from(DataTestCreator.shipment().snapshot()));
        final ArgumentCaptor<KafkaOutboxRecord> recordCaptor = ArgumentCaptor.forClass(KafkaOutboxRecord.class);

        listener.handle(event);

        verify(outboxPort).save(recordCaptor.capture());
        final KafkaOutboxRecord record = recordCaptor.getValue();
        verify(publicationService).publish(record);
        assertThat(record.topic()).isEqualTo("shipment.events");
        assertThat(record.eventType()).isEqualTo("shipment.changed");
        assertThat(record.eventVersion()).isEqualTo(1);
        assertThat(record.eventId()).isNotNull();
        assertThat(record.occurredAt()).isNotNull();
        assertThat(record.operatorId()).isEqualTo(OperatorId.of(7L));
        assertThat(record.headers())
                .containsEntry(KafkaEventHeaders.TYPE_ID, ShipmentChangedIntegrationEvent.class.getSimpleName())
                .containsEntry(KafkaEventHeaders.EVENT_TYPE, "shipment.changed")
                .containsEntry(KafkaEventHeaders.EVENT_VERSION, "1")
                .containsEntry(KafkaEventHeaders.OPERATOR_ID, "7")
                .containsEntry(KafkaEventHeaders.USER_ID, "42")
                .containsEntry(KafkaEventHeaders.DEPARTMENT_ID, "10");

        final JsonNode payload = objectMapper.readTree(record.payload());
        assertThat(payload.has("payload")).isTrue();
        assertThat(payload.path("payload").size()).isEqualTo(7);
        assertThat(payload.path("payload").path("eventType").asText()).isEqualTo("shipment.changed");
        assertThat(payload.path("payload").path("operatorId").path("value").asLong()).isEqualTo(7L);
        assertThat(payload.path("payload").path("userId").path("value").asLong()).isEqualTo(42L);
        assertThat(payload.path("payload").path("departmentId").path("value").asLong()).isEqualTo(10L);
        assertThat(payload.path("eventId").asText()).isEqualTo(record.eventId().toString());
        assertThat(payload.path("eventType").asText()).isEqualTo("shipment.changed");
        assertThat(payload.path("version").asInt()).isEqualTo(1);
        assertThat(payload.path("occurredAt").asText()).isEqualTo(record.occurredAt().toString());
        assertThat(payload.path("operatorId").path("value").asLong()).isEqualTo(7L);
        assertThat(payload.path("userId").path("value").asLong()).isEqualTo(42L);
        assertThat(payload.path("departmentId").path("value").asLong()).isEqualTo(10L);
        assertThat(payload.has("eventVersion")).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldStoreDomainEventInOutbox() throws Exception {
        final Environment environment = mock(Environment.class);
        final KafkaOutboxPort outboxPort = mock(KafkaOutboxPort.class);
        final KafkaOutboxPublicationService publicationService = mock(KafkaOutboxPublicationService.class);
        final OperatorContextProvider contextProvider = mock(OperatorContextProvider.class);
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        when(environment.getProperty("manager.kafka.domain-events.topic"))
                .thenReturn("shipment.domain-events");
        when(contextProvider.currentContext()).thenReturn(Optional.of(new OperatorDetails(
                OperatorId.of(7L), new UserId(42L), new DepartmentId(10L))));
        final IntegrationEventOutboxListener listener = new IntegrationEventOutboxListener(
                environment, objectMapper, outboxPort, publicationService, contextProvider);
        final Instant occurredAt = Instant.parse("2026-08-29T12:00:00Z");
        final ShipmentCreated event = new ShipmentCreated(DataTestCreator.shipment().snapshot(), occurredAt);
        final ArgumentCaptor<KafkaOutboxRecord> recordCaptor = ArgumentCaptor.forClass(KafkaOutboxRecord.class);

        listener.handle(event);

        verify(outboxPort).save(recordCaptor.capture());
        final KafkaOutboxRecord record = recordCaptor.getValue();
        verify(publicationService).publish(record);
        assertThat(record.topic()).isEqualTo("shipment.domain-events");
        assertThat(record.eventType()).isEqualTo(ShipmentCreated.class.getName());
        assertThat(record.eventVersion()).isEqualTo(1);
        assertThat(record.occurredAt()).isEqualTo(occurredAt);
        assertThat(record.operatorId()).isEqualTo(OperatorId.of(7L));
        assertThat(record.headers())
                .containsEntry(KafkaEventHeaders.TYPE_ID, ShipmentCreated.class.getSimpleName())
                .containsEntry(KafkaEventHeaders.EVENT_TYPE, ShipmentCreated.class.getSimpleName())
                .containsEntry(KafkaEventHeaders.EVENT_CLASS, ShipmentCreated.class.getName())
                .containsEntry(KafkaEventHeaders.EVENT_VERSION, "1")
                .containsEntry(KafkaEventHeaders.OCCURRED_AT, occurredAt.toString());

        final JsonNode payload = objectMapper.readTree(record.payload());
        assertThat(payload.path("snapshot").isObject()).isTrue();
        assertThat(payload.path("eventType").asText()).isEqualTo(ShipmentCreated.class.getSimpleName());
        assertThat(payload.path("eventClass").asText()).isEqualTo(ShipmentCreated.class.getName());
        assertThat(payload.path("occurredAt").asText()).isEqualTo(occurredAt.toString());
    }
}
