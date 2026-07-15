package com.warehouse.shipment;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.application.TransactionalKafkaOutboxWriter;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.OutboxIntegrationEventPublisher;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReadModelChanged;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.application.event.snapshot.ShipmentReadModelData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OutboxIntegrationEventPublisherTest {

    @Test
    void shouldStoreBusinessPayloadAndTechnicalMetadataSeparately() throws Exception {
        final Environment environment = mock(Environment.class);
        final TransactionalKafkaOutboxWriter outboxWriter = mock(TransactionalKafkaOutboxWriter.class);
        final OperatorContextProvider contextProvider = mock(OperatorContextProvider.class);
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        when(environment.getProperty("manager.kafka.integration-events.routes.shipment.changed"))
                .thenReturn("shipment.events");
        when(contextProvider.currentContext()).thenReturn(Optional.of(new OperatorDetails(
                OperatorId.of(7L), new UserId(42L), new DepartmentId(10L))));
        final OutboxIntegrationEventPublisher publisher = new OutboxIntegrationEventPublisher(
                environment, objectMapper, outboxWriter, contextProvider);
        final ShipmentChangedIntegrationEvent event = new ShipmentChangedIntegrationEvent(
                ShipmentEventData.from(DataTestCreator.shipment().snapshot()));
        final ArgumentCaptor<KafkaOutboxRecord> recordCaptor = ArgumentCaptor.forClass(KafkaOutboxRecord.class);

        publisher.publish(event);

        verify(outboxWriter).append(recordCaptor.capture());
        final KafkaOutboxRecord record = recordCaptor.getValue();
        assertThat(record.topic()).isEqualTo("shipment.events");
        assertThat(record.messageKey()).isEqualTo(String.valueOf(event.payload().shipmentId().getValue()));
        assertThat(record.eventType()).isEqualTo("shipment.changed");
        assertThat(record.eventVersion()).isEqualTo(1);
        assertThat(record.eventId()).isNotNull();
        assertThat(record.occurredAt()).isNotNull();
        assertThat(record.operatorId()).isEqualTo(OperatorId.of(7L));
        assertThat(record.headers())
                .containsEntry(KafkaEventHeaders.TYPE_ID, ShipmentChangedIntegrationEvent.class.getName())
                .containsEntry(KafkaEventHeaders.EVENT_TYPE, "shipment.changed")
                .containsEntry(KafkaEventHeaders.EVENT_VERSION, "1")
                .containsEntry(KafkaEventHeaders.OPERATOR_ID, "7")
                .containsEntry(KafkaEventHeaders.USER_ID, "42")
                .containsEntry(KafkaEventHeaders.DEPARTMENT_ID, "10");

        final JsonNode payload = objectMapper.readTree(record.payload());
        assertThat(payload.has("payload")).isTrue();
        assertThat(payload.path("payload").size()).isEqualTo(21);
        assertThat(java.time.LocalDateTime.parse(payload.path("payload").path("updatedAt").asText()))
                .isEqualTo(event.payload().updatedAt());
        assertThat(payload.path("payload").has("eventType")).isFalse();
        assertThat(payload.path("payload").has("operatorId")).isFalse();
        assertThat(payload.path("payload").has("userId")).isFalse();
        assertThat(payload.path("payload").has("departmentId")).isFalse();
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
    void shouldStoreReadModelSyncEventWithShipmentKey() throws Exception {
        final Environment environment = mock(Environment.class);
        final TransactionalKafkaOutboxWriter outboxWriter = mock(TransactionalKafkaOutboxWriter.class);
        final OperatorContextProvider contextProvider = mock(OperatorContextProvider.class);
        when(environment.getProperty("manager.kafka.integration-events.routes.shipment.read-model.changed"))
                .thenReturn("shipment.read-model.sync");
        when(contextProvider.currentContext()).thenReturn(Optional.empty());
        final OutboxIntegrationEventPublisher publisher = new OutboxIntegrationEventPublisher(
                environment, new ObjectMapper().findAndRegisterModules(), outboxWriter, contextProvider);
        final ShipmentReadModelData snapshot = new ShipmentReadModelData(
                DataTestCreator.shipment().getShipmentId());
        final ShipmentReadModelChanged event = new ShipmentReadModelChanged(snapshot, java.time.Instant.EPOCH);
        final ArgumentCaptor<KafkaOutboxRecord> recordCaptor = ArgumentCaptor.forClass(KafkaOutboxRecord.class);

        publisher.publish(event);

        verify(outboxWriter).append(recordCaptor.capture());
        final KafkaOutboxRecord record = recordCaptor.getValue();
        assertThat(record.topic()).isEqualTo("shipment.read-model.sync");
        assertThat(record.messageKey()).isEqualTo(String.valueOf(event.snapshot().shipmentId().getValue()));
        assertThat(record.eventType()).isEqualTo("shipment.read-model.changed");
        assertThat(record.headers())
                .containsEntry(KafkaEventHeaders.TYPE_ID, ShipmentReadModelChanged.class.getName());
        final JsonNode payload = new ObjectMapper().findAndRegisterModules().readTree(record.payload());
        assertThat(payload.path("snapshot").size()).isEqualTo(1);
        assertThat(payload.path("snapshot").path("shipmentId").path("value").asLong())
                .isEqualTo(snapshot.shipmentId().getValue());
    }

    @Test
    void shouldRejectIntegrationEventWithoutContractMetadata() {
        final TransactionalKafkaOutboxWriter outboxWriter = mock(TransactionalKafkaOutboxWriter.class);
        final OutboxIntegrationEventPublisher publisher = new OutboxIntegrationEventPublisher(
                mock(Environment.class),
                new ObjectMapper(),
                outboxWriter,
                mock(OperatorContextProvider.class));
        final IntegrationEvent eventWithoutType = new IntegrationEvent() {
        };

        assertThatThrownBy(() -> publisher.publish(eventWithoutType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not define its type");
        verify(outboxWriter, never()).append(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldBeSharedSecondaryPortAdapterWithoutAnotherSpringEventHop() {
        assertThat(IntegrationEventPublisher.class).isAssignableFrom(OutboxIntegrationEventPublisher.class);

        final boolean hasSpringEventHandler = Arrays.stream(OutboxIntegrationEventPublisher.class.getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(EventListener.class)
                        || method.isAnnotationPresent(TransactionalEventListener.class));
        assertThat(hasSpringEventHandler).isFalse();
    }
}
