package com.warehouse.routetracker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedIntegrationEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentEventData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.messaging.Message;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentKafkaConfigurationTest {

    private static final LocalDateTime UPDATED_AT = LocalDateTime.parse("2026-08-11T10:15:30");

    @Test
    void shouldDeserializeFromListenerTypeWithoutProducerTypeHeader() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final RecordMessageConverter converter = new ShipmentKafkaConfiguration()
                .shipmentKafkaRecordMessageConverter(objectMapper);
        final ShipmentChangedIntegrationEvent expected = new ShipmentChangedIntegrationEvent(
                shipmentEventData(),
                "shipment.created",
                new OperatorId(7L),
                new DepartmentId(10L),
                new UserId(42L)
        );
        final String producerJson = """
                {
                  "eventId": "f783d37e-06e9-4efc-8f18-099343b150e8",
                  "eventType": "shipment.created",
                  "version": 1,
                  "occurredAt": "2026-08-11T10:15:30Z",
                  "operatorId": {"value": 7},
                  "departmentId": {"value": 10},
                  "userId": {"value": 42},
                  "payload": {
                    "shipmentId": {"value": 123},
                    "shipmentStatus": "CREATED",
                    "updatedAt": "2026-08-11T10:15:30"
                  }
                }
                """;
        final ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "shipment.events", 0, 0L, "123", producerJson);

        final Message<?> converted = converter.toMessage(
                record,
                null,
                null,
                ShipmentChangedIntegrationEvent.class
        );

        final ShipmentChangedIntegrationEvent event = (ShipmentChangedIntegrationEvent) converted.getPayload();
        assertThat(record.headers().lastHeader("__TypeId__")).isNull();
        assertThat(event.payload()).isEqualTo(expected.payload());
        assertThat(event.eventType()).isEqualTo(expected.eventType());
        assertThat(event.userId()).isEqualTo(expected.userId());
        assertThat(event.departmentId()).isEqualTo(expected.departmentId());
        assertThat(event.operatorId()).isEqualTo(expected.operatorId());
    }

    private ShipmentEventData shipmentEventData() {
        return new ShipmentEventData(
                new com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentId(123L),
                null,
                null,
                null,
                null,
                null,
                com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentStatus.CREATED,
                null,
                null,
                null,
                null,
                UPDATED_AT,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
